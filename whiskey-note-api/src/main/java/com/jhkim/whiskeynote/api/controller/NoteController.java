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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<Void> createNote(
            @Valid @ModelAttribute NoteCreateRequest noteCreateRequest,
            User user
    ){
        noteService.createNote(noteCreateRequest, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long noteId,
            User user
    ){
        noteService.deleteNote(noteId, user);
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
    @GetMapping
    public ResponseEntity<List<NoteDetailResponse>> getNotes(
            @RequestParam("notebook") Long notebookId,
            User user
    ){

        return new ResponseEntity<>(
                noteService.getNotes(notebookId, user),
                HttpStatus.OK
        );
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDetailResponse> updateNote(
            @PathVariable Long noteId,
            @Valid @ModelAttribute NoteCreateRequest noteCreateRequest,
            User user
    ){
        noteService.updateNote(noteId, noteCreateRequest, user);
        return new ResponseEntity<>(
                noteService.updateNote(noteId, noteCreateRequest, user),
                HttpStatus.OK
        );
    }

}
