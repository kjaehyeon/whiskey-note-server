package com.jhkim.whiskeynote.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiskeyColor {
    CLEAR("clear","투명",0),
    STRAW("straw","옅은노랑",1),
    HONEY("honey","꿀",2),
    GOLD("gold","금빛",3),
    AMBER("amber","호박",4),
    CARAMEL("caramel","캐러멜",5),
    MAHOGANY("mahogany","마호가니",6);

    private final String name;
    private final String name_ko;
    private final Integer level;

    @JsonCreator
    public static WhiskeyColor from(String value){
        for(WhiskeyColor whiskeyColor : WhiskeyColor.values()){
            if(whiskeyColor.getName().equals(value)){
                return whiskeyColor;
            }
        }
        throw new GeneralException(ErrorCode.ENUM_TYPE_MISMATCH);
    }
}
