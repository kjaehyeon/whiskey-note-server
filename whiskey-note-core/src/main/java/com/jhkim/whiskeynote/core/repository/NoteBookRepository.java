package com.jhkim.whiskeynote.core.repository;


import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteBookRepository extends JpaRepository<NoteBook, Long> {
    List<NoteBook> findNoteBookByWriter(User writer);
    Optional<NoteBook> findNoteBookById(Long id);

//    @Modifying
//    @Query("DELETE FROM NoteBook WHERE id = ?1")
//    Integer deleteNoteBookById(Long id);
}
