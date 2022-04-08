package com.jhkim.whiskeynote.core.repository;


import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteBookRepository extends JpaRepository<NoteBook, Long> {
    List<NoteBook> findNoteBookByWriter(User writer);
}
