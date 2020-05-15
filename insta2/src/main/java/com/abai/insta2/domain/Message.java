package com.abai.insta2.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity //даем знать Что это сущности
@Table  // и искал данные в таблице
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = {"id"})
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    // датау публикации делаем не обновляемым
    @Column(updatable = false)
    //меняем формат отображения
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH-mm-ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime publicationDate;
    @JsonView(Views.FullMessage.class)
    private String link;
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public String getLinkCover() {
        return linkCover;
    }

    public void setLinkCover(String linkCover) {
        this.linkCover = linkCover;
    }
}
