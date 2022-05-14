package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bool3 {
    NO("NO"),
    YES("YES"),
    UNKNOWN("UNKNOWN");
    private final String name_en;
}
