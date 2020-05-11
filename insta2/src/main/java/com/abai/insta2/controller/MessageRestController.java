package com.abai.insta2.controller;

import com.abai.insta2.domain.Message;
import com.abai.insta2.domain.Views;
import com.abai.insta2.dto.EventType;
import com.abai.insta2.dto.ObjectType;
import com.abai.insta2.repo.MessageRepo;
import com.abai.insta2.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController            // даем знать Spring что это rest controller
@RequestMapping("message") // mapping после основного id
public class MessageRestController {
    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageRestController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    /* @ GetMapping используется для обработки метода запроса типа GET
    * получаем весь список сообщений
    */
    @GetMapping
    //jsonView будем видеть только те которые мы пометили
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    /* Для работы с параметрами, передаваемыми через адрес запроса в Spring WebMVC используется аннотация @PathVariable.
    * полуаем сообщения по id
     */
    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOneMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
       message.setPublicationDate(LocalDateTime.now());
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,
                          @RequestBody Message message
    ) {
        //copyProperties() метод из класса BeanUtils скопироует все
        //из message в messageFromDb все поля кроме "id"
        BeanUtils.copyProperties(message, messageFromDb, "id");

        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }
}
