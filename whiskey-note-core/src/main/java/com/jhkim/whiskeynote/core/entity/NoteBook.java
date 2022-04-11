package com.jhkim.whiskeynote.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Table(name = "note_book")
@Entity
public class NoteBook extends BaseEntity{
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    //표지 색상 RGB
    private Integer red;
    private Integer green;
    private Integer blue;

}
