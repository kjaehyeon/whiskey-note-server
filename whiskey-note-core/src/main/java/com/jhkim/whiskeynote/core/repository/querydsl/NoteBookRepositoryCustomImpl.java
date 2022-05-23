package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;
import com.jhkim.whiskeynote.core.entity.QNote;
import com.jhkim.whiskeynote.core.entity.QNoteBook;
import com.jhkim.whiskeynote.core.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class NoteBookRepositoryCustomImpl implements NoteBookRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QNote note = QNote.note;
    private final QNoteBook noteBook = QNoteBook.noteBook;

    //TODO countDistinct는 느리므로 추후 subquery로 대체
    @Override
    public List<NoteBookDetailResponse> findNoteBookAndNoteCntByWriterName(String username){
        return queryFactory.select(
                    Projections.bean(
                            NoteBookDetailResponse.class,
                            noteBook.id.as("notebookId"),
                            noteBook.title,
                            noteBook.red,
                            noteBook.green,
                            noteBook.blue,
                            note.countDistinct().intValue().as("pageNum")
                    )
                )
                .from(noteBook, note)
                .rightJoin(note.notebook, noteBook)
                .where(noteBook.writer.username.eq(username))
                .groupBy(noteBook)
                .fetch();
    }
}
