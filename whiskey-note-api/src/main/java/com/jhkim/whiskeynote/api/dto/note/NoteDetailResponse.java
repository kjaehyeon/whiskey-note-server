package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class NoteDetailResponse {
    @NotNull
    private Long id;

    @NotNull
    private Long notebookId;
    private WhiskeyDetailResponse whiskey;

    @NotNull
    private String whiskeyName;
    private String distiller;
    private Integer price;
    private String color;

    @NotNull
    private Float rating;
    private Integer age;
    private String nose;
    private String taste;
    private String finish;

    @NotNull
    private String description;
    private Integer whiskeyColor;

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

    private List<String> imageUrls;

    public static NoteDetailResponse fromEntity(
            Note note,
            List<String> imageUrls
    ){

        return NoteDetailResponse.builder()
                .whiskey(WhiskeyDetailResponse.fromEntity(note.getWhiskey()))
                .notebookId(note.getNotebook().getId())
                .whiskeyName(note.getWhiskeyName())
                .distiller(note.getDistiller())
                .price(note.getPrice())
                .rating(note.getRating())
                .age(note.getAge())
                .nose(note.getNose())
                .taste(note.getTaste())
                .finish(note.getFinish())
                .description(note.getDescription())
                .color(note.getColor().getName_ko())
                .smokey(note.getSmokey())
                .peaty(note.getPeaty())
                .herbal(note.getHerbal())
                .briny(note.getBriny())
                .vanilla(note.getVanilla())
                .fruity(note.getFruity())
                .floral(note.getFloral())
                .woody(note.getWoody())
                .rich(note.getRich())
                .spicy(note.getSpicy())
                .sweet(note.getSweet())
                .salty(note.getSalty())
                .imageUrls(imageUrls)
                .build();
    }
}
