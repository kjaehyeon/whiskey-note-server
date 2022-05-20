package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteCreateRequest {
    @NotNull
    private Long notebookId;
    private Long whiskeyId;

    @NotNull
    private String whiskeyName;
    private String distiller;
    private Integer price;
    @NotNull
    private Float rating;
    private Integer age;
    private String nose;
    private String taste;
    private String finish;

    @NotNull
    private String description;

    private String color;
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

    private List<MultipartFile> images;

    public Note toEntity(
            User writer,
            Whiskey whiskey,
            NoteBook noteBook
    ){
        return Note.builder()
                .writer(writer)
                .whiskey(whiskey)
                .notebook(noteBook)
                .whiskeyName(whiskeyName)
                .distiller(distiller)
                .price(price)
                .rating(rating)
                .age(age)
                .nose(nose)
                .taste(taste)
                .finish(finish)
                .description(description)
                .color(WhiskeyColor.valueOf(color))
                .smokey(smokey)
                .peaty(peaty)
                .herbal(herbal)
                .briny(briny)
                .vanilla(vanilla)
                .fruity(fruity)
                .floral(floral)
                .woody(woody)
                .rich(rich)
                .spicy(spicy)
                .sweet(sweet)
                .salty(salty)
                .build();
    }

    public Note updateNoteEntity(
            Note note,
            Whiskey whiskey,
            NoteBook noteBook
    ) {
        if(whiskey != null) note.setWhiskey(whiskey);
        if(noteBook != null) note.setNotebook(noteBook);
        if(whiskeyName != null) note.setWhiskeyName(whiskeyName);
        if(distiller != null) note.setDistiller(distiller);
        if(price != null) note.setPrice(price);
        if(rating != null) note.setRating(rating);
        if(age != null) note.setAge(age);
        if(nose != null) note.setNose(nose);
        if(taste != null) note.setTaste(taste);
        if(finish != null) note.setFinish(finish);
        if(description != null) note.setDescription(description);
        if(color != null) note.setColor(WhiskeyColor.valueOf(color));
        if(smokey != null) note.setSmokey(smokey);
        if(peaty != null) note.setPeaty(peaty);
        if(herbal != null) note.setHerbal(herbal);
        if(briny != null) note.setBriny(briny);
        if(vanilla != null) note.setVanilla(vanilla);
        if(fruity != null) note.setFruity(fruity);
        if(floral != null) note.setFloral(floral);
        if(woody != null) note.setWoody(woody);
        if(rich != null) note.setRich(rich);
        if(spicy != null) note.setSpicy(spicy);
        if(sweet != null) note.setSweet(sweet);
        if(salty != null) note.setSalty(salty);
        return note;
    }
}