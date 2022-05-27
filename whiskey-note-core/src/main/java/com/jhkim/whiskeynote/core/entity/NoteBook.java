package com.jhkim.whiskeynote.core.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Table(name = "note_book")
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
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
