package com.jhkim.whiskeynote.api.dto.whiskey;

import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class WhiskeyDetailResponse {
    private Long whiskeyId;
    private String brand;
    private String name;
    private String distillery;

    private String category;
    private String district;

    private String bottler;
    private Integer statedAge;//숙성년수
    private LocalDate vintage;
    private String caskType;

    private Float alc;
    private Integer retailPrice;
    private Integer size; //밀리리터
    private String bottledFor;

    private String isColored;
    private String isChillFiltered;
    private String isSingleCask;
    private String isCaskStrength;
    private String isSmallBatch;

    private List<String> imageUrls;

    public static WhiskeyDetailResponse fromEntity(Whiskey whiskey) {
        return WhiskeyDetailResponse.builder()
                .whiskeyId(whiskey.getId())
                .brand(whiskey.getBrand())
                .name(whiskey.getName())
                .distillery(whiskey.getDistillery())
                .category(whiskey.getCategory().getName_ko())
                .district(whiskey.getDistrict().getName_ko())
                .bottler(whiskey.getBottler())
                .statedAge(whiskey.getStatedAge())
                .vintage(whiskey.getVintage())
                .caskType(whiskey.getCaskType())
                .alc(whiskey.getAlc())
                .retailPrice(whiskey.getRetailPrice())
                .size(whiskey.getSize())
                .bottledFor(whiskey.getBottledFor())
                .isColored(whiskey.getIsColored().getName_en())
                .isChillFiltered(whiskey.getIsChillfiltered().getName_en())
                .isSingleCask(whiskey.getIsSingleCask().getName_en())
                .isCaskStrength(whiskey.getIsCaskStrength().getName_en())
                .isSmallBatch(whiskey.getIsSmallBatch().getName_en())
                .build();
    }
}
