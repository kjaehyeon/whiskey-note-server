package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteDetailResponse {
    @NotNull
    private Long id;

    @NotNull
    private Long notebook_id;
    private WhiskeyDetailResponse whiskey;

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

    private List<String> image_urls;

    public static NoteDetailResponse fromEntity(Note note, List<String> imageUrls){

        return NoteDetailResponse.builder()
                .image_urls(imageUrls)
                .build();
    }
}
