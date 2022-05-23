package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.entity.QNoteImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoteImageRepositoryCustomImpl implements NoteImageRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QNoteImage noteImage = QNoteImage.noteImage;

    @Override
    public void deleteAllByNote_id(Long noteId) {
        queryFactory.delete(noteImage)
                .where(noteImage.note.id.eq(noteId))
                .execute();
    }
}
