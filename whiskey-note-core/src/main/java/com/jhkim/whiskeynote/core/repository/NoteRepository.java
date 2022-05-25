package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.querydsl.NoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends
        JpaRepository<Note, Long>,
        NoteRepositoryCustom
{
    List<Note> findAllByNotebook(NoteBook noteBook);
    Optional<Note> findNoteById(Long noteId);
    List<Note> findAllByWriter(User writer);

    @Modifying
    @Query("DELETE FROM Note WHERE id = ?1")
    Integer deleteNoteById(Long noteId);
}
