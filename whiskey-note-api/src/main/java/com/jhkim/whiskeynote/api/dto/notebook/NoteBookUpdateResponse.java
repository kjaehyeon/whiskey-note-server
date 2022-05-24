package com.jhkim.whiskeynote.api.dto.notebook;

import com.jhkim.whiskeynote.core.entity.NoteBook;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class NoteBookUpdateResponse {
    private String title;

    private Integer red;
    private Integer green;
    private Integer blue;

    public static NoteBookUpdateResponse fromEntity(NoteBook noteBook){
        return NoteBookUpdateResponse.of(
                noteBook.getTitle(),
                noteBook.getRed(),
                noteBook.getGreen(),
                noteBook.getBlue()
        );
    }
}
