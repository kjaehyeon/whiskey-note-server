package com.jhkim.whiskeynote.api.dto.notebook;

import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor(staticName = "of")
public class NoteBookCreateRequest {
    @NotBlank
    private String title;

    @NotNull
    @Range(min = 0, max = 255)
    private Integer red;

    @NotNull
    @Range(min = 0, max = 255)
    private Integer green;

    @NotNull
    @Range(min = 0, max = 255)
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
        noteBook.setTitle(title);
        noteBook.setRed(red);
        noteBook.setGreen(green);
        noteBook.setBlue(blue);

        return noteBook;
    }
}
