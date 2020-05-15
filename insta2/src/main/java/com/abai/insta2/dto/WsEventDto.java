package com.abai.insta2.dto;

import com.abai.insta2.domain.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonView(Views.Id.class)
public class WsEventDto {

     /*Data Transfer Object (DTO) — один из шаблонов проектирования,
     используется для передачи данных между подсистемами приложения.
     Data Transfer Object, в отличие от business object или
     data access object не должен содержать какого-либо поведения*/

    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;

    public WsEventDto(ObjectType objectType, EventType eventType, String body) {
        this.objectType = objectType;
        this.eventType = eventType;
        this.body = body;
    }
}
