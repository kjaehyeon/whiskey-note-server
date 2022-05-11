package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.api.service.NoteService;
import com.jhkim.whiskeynote.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @ModelAttribute NoteCreateRequest noteCreateRequest,
            User user
    ){
        noteService.create(noteCreateRequest, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long noteId,
            User user
    ){
        noteService.delete(noteId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDetailResponse> getNote(
            @PathVariable Long noteId,
            User user
    ){
        return new ResponseEntity<>(
                noteService.getNote(noteId, user),
                HttpStatus.OK
        );
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDetailResponse> updateNote(
            @PathVariable Long noteId,
            @Valid @ModelAttribute NoteCreateRequest noteCreateRequest,
            User user
    ){
        noteService.upsert(noteId, noteCreateRequest, user);
        return new ResponseEntity<>(
                noteService.upsert(noteId, noteCreateRequest, user),
                HttpStatus.OK
        );
    }

}
