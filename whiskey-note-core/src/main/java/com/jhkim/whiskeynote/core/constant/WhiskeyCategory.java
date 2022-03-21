package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiskeyCategory {
    UNKNOWN(""),
    SINGLE_MALT("싱글몰트"),
    BOURBON("버번"),
    BLENDED("블렌디드"),
    BLENDED_MALT("블렌디드 몰트"),
    TENNESSEE("테네시"),
    SINGLE_GRAIN("싱글그레인"),
    BLENDED_GRAIN("블렌디드 그레인"),
    WHEAT("밀"),
    RYE("호밀"),
    CORN("콘"),
    SPIRIT("스피릿"),
    RICE("라이스"),
    AMERICAN_WHISKEY("아메리칸 위스키"),
    CANADIAN_WHISKEY("케네디안 위스키");

    private final String name_ko;
}
