package com.jhkim.whiskeynote.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteDto {

    private Long notebook_id;
    private Long whiskey_id;

    private String whiskey_name;
    private String distiller;
    private Integer price;
    private Float rating;
    private Integer age;

    private String nose;
    private String taste;
    private String finish;

    private String review;

    private Integer whiskey_color;

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