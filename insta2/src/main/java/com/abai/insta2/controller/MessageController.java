package com.abai.insta2.controller;

import com.abai.insta2.exeptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController            // даем знать Spring что это rest controller
@RequestMapping("message") // mapping после основного id
public class MessageController {
    private int counter = 6;
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add( new HashMap<String, String>() {{ put("id", "1"); put("text", "first Apple"); }});
        add( new HashMap<String, String>() {{ put("id", "2"); put("text", "second Silver"); }});
        add( new HashMap<String, String>() {{ put("id", "3"); put("text", "3rd Earth"); }});
        add( new HashMap<String, String>() {{ put("id", "4"); put("text", "4th Subaru"); }});
        add( new HashMap<String, String>() {{ put("id", "5"); put("text", "5th Rio"); }});
    }};

    /* @ GetMapping используется для обработки метода запроса типа GET
    * получаем весь список сообщений
    */
    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    /* Для работы с параметрами, передаваемыми через адрес запроса в Spring WebMVC используется аннотация @PathVariable.
    * полуаем сообщения по id
     */
    @GetMapping("{id}")
    public Map<String, String> getOneMessage(@PathVariable String id) {
        return getMessageFromId(id);
    }

    private Map<String, String> getMessageFromId(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    /* @ PostMapping используется для обработки метода запроса типа POST
    * добовление сообщений
     */
    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message){
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    /*
    * обновление сообшений
     */
    @PutMapping
    public Map<String, String> update(@PathVariable String id,
                                      @RequestBody Map<String, String> message){
        Map<String, String> messageFromDB = getMessageFromId(id);
        messageFromDB.putAll(message);
        messageFromDB.put("id", id);

        return messageFromDB;
    }

    @DeleteMapping
    public void delete(@PathVariable String id){
        Map<String, String> message = getOneMessage(id);
        message.remove(message);
    }
}
