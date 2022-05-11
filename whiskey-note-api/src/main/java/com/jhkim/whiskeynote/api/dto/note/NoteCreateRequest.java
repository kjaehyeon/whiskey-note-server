package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.core.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteCreateRequest {
    @NotNull
    private Long notebook_id;
    private Long whiskey_id;

    @NotNull
    private String whiskey_name;
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

    private List<MultipartFile> images;

    public Note toEntity(NoteCreateRequest noteCreateRequest){
        return Note.builder()
                .build();
    }
}