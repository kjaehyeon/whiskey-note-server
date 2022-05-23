package com.jhkim.whiskeynote.core.dto;

import com.jhkim.whiskeynote.core.entity.NoteBook;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class NoteBookDetailResponse {
    @NotNull
    private Long notebookId;

    @NotBlank
    private String title;

    private Integer red;
    private Integer green;
    private Integer blue;

    private Integer pageNum;

    public static NoteBookDetailResponse fromEntity(NoteBook noteBook, int pageNum){
        return NoteBookDetailResponse.builder()
                .notebookId(noteBook.getId())
                .title(noteBook.getTitle())
                .red(noteBook.getRed())
                .green(noteBook.getGreen())
                .blue(noteBook.getBlue())
                .pageNum(pageNum)
                .build();
    }
}
