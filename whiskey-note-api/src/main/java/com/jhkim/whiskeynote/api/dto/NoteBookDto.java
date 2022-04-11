package com.jhkim.whiskeynote.api.dto;

import com.jhkim.whiskeynote.core.entity.NoteBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteBookDto {
    @NotBlank
    private String title;

    private Integer red;
    private Integer green;
    private Integer blue;

    public NoteBook toEntity(){
        return NoteBook.of(
                title,
                null,
                red,
                green,
                blue
        );
    }
}