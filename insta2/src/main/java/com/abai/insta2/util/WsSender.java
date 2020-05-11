package com.abai.insta2.util;

import com.abai.insta2.dto.EventType;
import com.abai.insta2.dto.ObjectType;
import com.abai.insta2.dto.WsEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WsSender {
    // отвечает за отправку сообшений через очереди сообщений
    private final SimpMessagingTemplate template;
    // сериализует и дисериализует обьекты
    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }
/* Consumer это анонимная функция котрый может принимать один
* аргумент на входе, и ничего не возращать */
    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view){
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);

        return (EventType eventType, T payload) -> {
            String value = null;
            try {
                value = writer.writeValueAsString(payload);
            }catch (JsonProcessingException e){
                throw new RuntimeException(e);
            }
            template.convertAndSend(
                    "/topic/activity",
                    new WsEventDto(objectType, eventType, value)
            );
        };
    }
}
