package com.jhkim.whiskeynote.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Path {
    NOTE_IMAGE("note-image"),
    WHISKEY_IMAGE("whiskey-image");

    private final String folderName;
}
