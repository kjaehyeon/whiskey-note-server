package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.NoteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface NoteImageRepository extends JpaRepository<NoteImage, Long> {
    List<NoteImage> findNoteImageByNote_Id(Long note_Id);
    Integer deleteAllByNote_Id(Long note_Id);
}
