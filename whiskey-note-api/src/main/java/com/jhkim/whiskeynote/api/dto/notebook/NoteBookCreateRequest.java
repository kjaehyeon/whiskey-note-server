package com.jhkim.whiskeynote.api.dto.notebook;

import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor(staticName = "of")
public class NoteBookCreateRequest {
    @NotBlank
    private String title;

    private Integer red;
    private Integer green;
    private Integer blue;

    public NoteBook toEntity(User user){
        return NoteBook.of(
                title,
                user,
                red,
                green,
                blue
        );
    }

    public NoteBook updateEntity(NoteBook noteBook){
        if(title != null) noteBook.setTitle(title);
        if(red != null) noteBook.setRed(red);
        if(green != null) noteBook.setGreen(green);
        if(blue != null) noteBook.setBlue(blue);

        return noteBook;
    }
}
