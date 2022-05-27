package com.jhkim.whiskeynote.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Table(name = "note_image")
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity
public class NoteImage extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(nullable = false)
    private String url;
}
