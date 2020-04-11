package com.abai.insta2.controller;

import com.abai.insta2.domain.Message;
import com.abai.insta2.domain.Views;
import com.abai.insta2.repo.MessageRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController            // даем знать Spring что это rest controller
@RequestMapping("message") // mapping после основного id
public class MessageRestController {
    private final MessageRepo messageRepo;

    @Autowired
    public MessageRestController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
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
       return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb,
                          @RequestBody Message message) {
        //copyProperties() метод из класса BeanUtils скопироует все
        //из message в messageFromDb все поля кроме "id"
        BeanUtils.copyProperties(message, messageFromDb, "id");

        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }
}
