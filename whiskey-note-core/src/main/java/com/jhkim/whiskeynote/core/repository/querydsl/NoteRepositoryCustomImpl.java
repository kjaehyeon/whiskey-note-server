package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class NoteRepositoryCustomImpl implements NoteRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    private final QNote note = QNote.note;
    private final QNoteBook notebook = QNoteBook.noteBook;
    private final QWhiskey whiskey = QWhiskey.whiskey;
    private final QUser user = QUser.user;

    @Override
    public Optional<Note> findNoteById(Long noteId){
        Note result =
                queryFactory.selectFrom(note)
                        .where(note.id.eq(noteId))
                        .fetchFirst();

        return result == null ? Optional.empty() : Optional.of(result);
    }
}
