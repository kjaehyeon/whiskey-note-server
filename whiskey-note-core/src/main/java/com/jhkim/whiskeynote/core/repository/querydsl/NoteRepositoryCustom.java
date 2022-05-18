package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NoteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QNote note = QNote.note;
    private final QNoteBook notebook = QNoteBook.noteBook;
    private final QWhiskey whiskey = QWhiskey.whiskey;
    private final QUser user = QUser.user;

    public Optional<Note> findNoteById(Long noteId){
        Note result = queryFactory.selectFrom(note)
                .join(note.writer, user).fetchJoin()
                .join(note.whiskey, whiskey).fetchJoin()
                .join(note.notebook, notebook).fetchJoin()
                .where(note.id.eq(noteId))
                .fetchOne();
        return result == null ? Optional.empty() : Optional.of(result);
    }
}
