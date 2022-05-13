package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.NoteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteImageRepository extends JpaRepository<NoteImage, Long> {
    @Query("SELECT ni FROM NoteImage ni JOIN FETCH ni.note WHERE ni.note.id = :noteId")
    List<NoteImage> findNoteImageByNote_Id(Long noteId);
    @Query("SELECT ni FROM NoteImage ni JOIN FETCH ni.note WHERE ni.note.id = :noteId")
    void deleteAllByNote_Id(Long noteId);
}
