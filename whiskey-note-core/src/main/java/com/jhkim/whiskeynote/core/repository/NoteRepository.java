package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByNotebook(NoteBook noteBook);

    @Modifying
    @Query("DELETE FROM Note WHERE id = ?1")
    Integer deleteNoteById(Long id);
}
