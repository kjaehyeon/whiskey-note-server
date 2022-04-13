package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByNotebook(NoteBook noteBook);
}
