package com.abai.insta2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MetaDto {

    /*Data Transfer Object (DTO) — один из шаблонов проектирования,
     используется для передачи данных между подсистемами приложения.
     Data Transfer Object, в отличие от business object или
      data access object не должен содержать какого-либо поведения*/

    private String title;
    private String description;
    private String cover;

    public MetaDto(String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
