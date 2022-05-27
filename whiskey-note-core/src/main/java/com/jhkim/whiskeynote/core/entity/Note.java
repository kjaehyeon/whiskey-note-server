package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Table(name = "note")
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
public class Note extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "note_book_id")
    private NoteBook notebook;

    @ManyToOne
    @JoinColumn(name = "whiskey_id")
    private Whiskey whiskey;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Column(nullable = false)
    private String whiskeyName;
    @Column(nullable = false)
    private Float rating;

    private String distiller;
    private Integer price;
    private Integer age;

    private String nose;
    private String taste;
    private String finish;
    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(value = EnumType.STRING)
    private WhiskeyColor color;

    private Integer smokey;
    private Integer peaty;
    private Integer herbal;
    private Integer briny;
    private Integer vanilla;
    private Integer fruity;
    private Integer floral;
    private Integer woody;
    private Integer rich;
    private Integer spicy;
    private Integer sweet;
    private Integer salty;
}
