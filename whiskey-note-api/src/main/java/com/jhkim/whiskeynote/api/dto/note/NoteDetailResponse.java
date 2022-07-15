package com.jhkim.whiskeynote.api.dto.note;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.Note;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class NoteDetailResponse {
    @NotNull
    private Long id;

    @NotNull
    private Long notebookId;
    private Long whiskeyId;
    private String writer;

    @NotNull
    private String whiskeyName;
    private String distiller;
    private Integer price;
    private WhiskeyColor whiskeyColor;

    @NotNull
    private Float rating;
    private Integer age;
    private String nose;
    private String taste;
    private String finish;

    @NotNull
    private String description;

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

    public static NoteDetailResponse fromEntityAndImageUrls(
            Note note,
            List<String> imageUrls
    ){
        return NoteDetailResponse.builder()
                .id(note.getId())
                .writer(note.getWriter().getUsername())
                .whiskeyId(note.getWhiskey().getId())
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
                .whiskeyColor(note.getColor())
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
