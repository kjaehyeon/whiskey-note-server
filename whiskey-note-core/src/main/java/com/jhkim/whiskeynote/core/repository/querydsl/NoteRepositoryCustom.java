package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.entity.Note;

import java.util.Optional;

public interface NoteRepositoryCustom {
    Optional<Note> findNoteById(Long noteId);
}
