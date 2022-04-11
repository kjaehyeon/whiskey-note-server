package com.jhkim.whiskeynote.api.dto;

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
}
