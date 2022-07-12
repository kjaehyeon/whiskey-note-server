package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@Table(name = "whiskey")
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
public class Whiskey extends BaseEntity{
    private String brand;
    private String name;
    private String distillery;

    @Enumerated(value = EnumType.STRING)
    private WhiskeyCategory category;
    @Enumerated(value = EnumType.STRING)
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
}
