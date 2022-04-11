package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiskeyColor {
    CLEAR("투명",0),
    STRAW("옅은노랑",1),
    HONEY("꿀",2),
    GOLD("금빛",3),
    AMBER("호박",4),
    CARAMEL("캐러멜",5),
    MAHOGANY("마호가니",6);

    private final String name_ko;
    private final Integer level;
}
