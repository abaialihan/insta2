package com.abai.insta2.controller;

import com.abai.insta2.domain.Message;
import com.abai.insta2.domain.Views;
import com.abai.insta2.dto.EventType;
import com.abai.insta2.dto.MetaDto;
import com.abai.insta2.dto.ObjectType;
import com.abai.insta2.repo.MessageRepo;
import com.abai.insta2.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController            // даем знать Spring что это rest controller
@RequestMapping("message") // mapping после основного id
public class MessageRestController {
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageRestController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    /* @ GetMapping используется для обработки метода запроса типа GET
      получаем весь список сообщений */
    @GetMapping
    //jsonView будем видеть только те которые мы пометили
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    /* Для работы с параметрами, передаваемыми через адрес запроса
     в Spring WebMVC используется аннотация @PathVariable.
     полуаем сообщения по id */
    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOneMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) throws IOException {
       message.setPublicationDate(LocalDateTime.now());
       fillMeta(message);
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,
                          @RequestBody Message message
    ) throws IOException {
        /* copyProperties() метод из класса BeanUtils скопироует все
          из message в messageFromDb все поля кроме "id" */
        BeanUtils.copyProperties(message, messageFromDb, "id");
        fillMeta(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);

            message.setLink(url);

            if (matcher.find()) {
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        /*jsoup – это библиотека на основе Java для работы с контентом на основе HTML.
         Он предоставляет очень удобный API для извлечения и обработки данных,
         используя лучшие методы DOM, CSS и jquery-подобные.
         Он реализует спецификацию WHATWG HTML5 и анализирует HTML в том же DOM,
         что и современные браузеры*/

        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }
}
