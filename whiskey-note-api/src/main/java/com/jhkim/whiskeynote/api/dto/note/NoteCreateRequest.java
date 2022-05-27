package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteCreateRequest {
    @NotNull
    private Long notebookId;
    private Long whiskeyId;

    @NotBlank
    private String whiskeyName;
    private String distiller;
    private Integer price;

    @Range(min=0, max=5)
    private Float rating;
    @Min(value = 0)
    private Integer age;
    private String nose;
    private String taste;
    private String finish;

    @NotNull
    private String description;

    private WhiskeyColor whiskeyColor;

    @Range(min=0, max=100)
    private Integer smokey;
    @Range(min=0, max=100)
    private Integer peaty;
    @Range(min=0, max=100)
    private Integer herbal;
    @Range(min=0, max=100)
    private Integer briny;
    @Range(min=0, max=100)
    private Integer vanilla;
    @Range(min=0, max=100)
    private Integer fruity;
    @Range(min=0, max=100)
    private Integer floral;
    @Range(min=0, max=100)
    private Integer woody;
    @Range(min=0, max=100)
    private Integer rich;
    @Range(min=0, max=100)
    private Integer spicy;
    @Range(min=0, max=100)
    private Integer sweet;
    @Range(min=0, max=100)
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
                .color(whiskeyColor)
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
        if(whiskeyColor != null) note.setColor(whiskeyColor);
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