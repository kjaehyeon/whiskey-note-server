package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;

import java.util.List;

public interface NoteBookRepositoryCustom {
    List<NoteBookDetailResponse> findNoteBookAndNoteCntByWriterName(String username);
}
