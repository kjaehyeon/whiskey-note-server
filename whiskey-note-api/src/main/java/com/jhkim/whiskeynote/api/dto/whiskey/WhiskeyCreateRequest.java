package com.jhkim.whiskeynote.api.dto.whiskey;

import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class WhiskeyCreateRequest {
    private String brand;
    private String name;
    private String distillery;

    private String category;
    private String district;

    private String bottler;
    private Integer statedAge;//숙성년수
    private Integer vintage;
    private String caskType;

    private Float alc;
    private Integer retailPrice;
    private Integer size; //밀리리터
    private String bottledFor;

    //@Range(min=0, max=2)
    private String isColored;
    private String isChillfiltered;
    private String isSingleCask;
    private String isCaskStrength;
    private String isSmallBatch;

    public Whiskey toEntity(){
        return Whiskey.builder()
                .brand(brand)
                .name(name)
                .distillery(distillery)
                .category(WhiskeyCategory.valueOf(category))
                .district(WhiskeyDistrict.valueOf(district))
                .bottler(bottler)
                .statedAge(statedAge)
                .vintage(vintage)
                .caskType(caskType)
                .alc(alc)
                .retailPrice(retailPrice)
                .size(size)
                .bottledFor(bottledFor)
                .isColored(Bool3.valueOf(isColored))
                .isChillfiltered(Bool3.valueOf(isChillfiltered))
                .isSingleCask(Bool3.valueOf(isSingleCask))
                .isCaskStrength(Bool3.valueOf(isCaskStrength))
                .isSmallBatch(Bool3.valueOf(isSmallBatch))
                .build();
    }
}
