package com.jhkim.whiskeynote.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "note_image")
@Entity
public class NoteImage extends BaseEntity{
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "whiskey_id")
    private Note note;

    @Column(nullable = false)
    private String url;
}
