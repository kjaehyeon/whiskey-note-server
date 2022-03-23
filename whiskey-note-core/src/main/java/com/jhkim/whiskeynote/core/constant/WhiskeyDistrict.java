package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiskeyDistrict {
    UNKNOWN(""),
    SPEYSIDE("스페이사이드"),
    ISLAY("아일라"),
    HIGHLAND("하이랜드"),
    LOWLAND("로우랜드"),
    CAMPBELTOWN("캠벨타운"),
    IRISH("아이리쉬"),
    ISLE_OF_SKYE("스카이섬"),
    ORKNEY("오크니"),
    ARRAN("아란"),
    ISLE_OF_JURA("주라섬"),
    ISLE_OF_MULL("뮬섬");

    private final String name_ko;
}
