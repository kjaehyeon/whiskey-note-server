package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "whiskey")
@Entity
@EqualsAndHashCode(callSuper = false)
public class Whiskey extends BaseEntity{

    private String brand;
    private String name;
    private String distillery;

    @Enumerated(value = EnumType.STRING)
    private WhiskeyCategory category;
    @Enumerated(value = EnumType.STRING)
    private WhiskeyDistrict district;

    private String bottler;
    private Integer stated_age;//숙성년수
    private Integer vintage;
    private String cask_type;

    private Float alc;
    private Integer retail_price;
    private Integer size; //밀리리터
    private String bottled_for;

    private Bool3 colored;
    private Bool3 chillfilterd;
    private Bool3 single_cask;
    private Bool3 cask_strength;
    private Bool3 small_batch;
}
