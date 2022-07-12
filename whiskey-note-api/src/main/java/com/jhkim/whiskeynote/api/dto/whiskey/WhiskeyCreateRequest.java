package com.jhkim.whiskeynote.api.dto.whiskey;

import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class WhiskeyCreateRequest {
    private String brand;
    private String name;
    private String distillery;

    private WhiskeyCategory category;
    private WhiskeyDistrict district;

    private String bottler;
    private Integer statedAge;//숙성년수
    private LocalDate vintage;
    private String caskType;

    private Float alc;
    private Integer retailPrice;
    private Integer size; //밀리리터
    private String bottledFor;

    private Bool3 isColored;
    private Bool3 isChillfiltered;
    private Bool3 isSingleCask;
    private Bool3 isCaskStrength;
    private Bool3 isSmallBatch;

    private List<MultipartFile> images;

    public Whiskey toEntity(){
        return Whiskey.builder()
                .brand(brand)
                .name(name)
                .distillery(distillery)
                .category(category)
                .district(district)
                .bottler(bottler)
                .statedAge(statedAge)
                .vintage(vintage)
                .caskType(caskType)
                .alc(alc)
                .retailPrice(retailPrice)
                .size(size)
                .bottledFor(bottledFor)
                .isColored(isColored)
                .isChillfiltered(isChillfiltered)
                .isSingleCask(isSingleCask)
                .isCaskStrength(isCaskStrength)
                .isSmallBatch(isSmallBatch)
                .build();
    }
}
