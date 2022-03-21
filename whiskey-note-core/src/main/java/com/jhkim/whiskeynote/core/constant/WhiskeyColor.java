package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiskeyColor {
    CLEAR("투명"),
    STRAW("옅은노랑"),
    HONEY("꿀"),
    GOLD("금빛"),
    AMBER("호박"),
    CARAMEL("캐러멜"),
    MAHOGANY("마호가니");

    private final String name_ko;
}
