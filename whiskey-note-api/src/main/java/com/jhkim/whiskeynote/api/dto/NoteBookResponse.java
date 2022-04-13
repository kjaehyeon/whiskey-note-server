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
public class NoteBookResponse {
    @NotNull
    private Long notebook_id;

    @NotBlank
    private String title;

    private Integer red;
    private Integer green;
    private Integer blue;

    //private Integer page_num;

    public static NoteBookResponse fromEntity(NoteBook noteBook){
        return NoteBookResponse.builder()
                .notebook_id(noteBook.getId())
                .title(noteBook.getTitle())
                .red(noteBook.getRed())
                .green(noteBook.getGreen())
                .blue(noteBook.getBlue())
                .build();
    }
}
