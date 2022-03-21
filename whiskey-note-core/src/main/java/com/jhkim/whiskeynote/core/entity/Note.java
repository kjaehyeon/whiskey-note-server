package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "note")
@Entity
public class Note extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;

    @OneToOne
    @JoinColumn(name = "whiskey_id")
    private Whiskey whiskey;

    private String whiskey_name;
    private String distiller;
    private Integer price;
    private Float rating;
    private Integer age;

    private String nose;
    private String taste;
    private String finish;

    @Lob
    private String review;

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
