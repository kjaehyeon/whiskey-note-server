package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByNotebook(NoteBook noteBook);
    Optional<Note> findNoteById(Long noteId);

    @Query("SELECT nt FROM Note nt JOIN FETCH nt.notebook WHERE nt.notebook.id = :notebookId")
    List<Note> findAllByNotebook_Id(Long notebookId);

    @Modifying
    @Query("DELETE FROM Note WHERE id = ?1")
    Integer deleteNoteById(Long noteId);
}
