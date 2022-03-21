package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.Bool_3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "whiskey")
@Entity
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

    private Bool_3 colored;
    private Bool_3 chillfilterd;
    private Bool_3 single_cask;
    private Bool_3 cask_strength;
    private Bool_3 small_batch;
}
