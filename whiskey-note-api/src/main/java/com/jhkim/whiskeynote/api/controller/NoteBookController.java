package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.NoteBookDto;
import com.jhkim.whiskeynote.api.dto.NoteDto;
import com.jhkim.whiskeynote.api.service.NoteBookService;
import com.jhkim.whiskeynote.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notebook")
public class NoteBookController {

    private final NoteBookService noteBookService;

    @PostMapping
    public ResponseEntity<Void> createNoteBook(
           @Valid @RequestBody NoteBookDto noteBookDto
    ){
        return null;
    }

    @PutMapping("/api/notebook/{notebookId}")
    public ResponseEntity<NoteBookDto> updateNoteBook(
            @Valid @RequestBody NoteBookDto noteBookDto,
            @PathVariable Long notebookId,
            Authentication authentication
    ){
        return null;
    }

    @GetMapping("/api/notebook")
    public ResponseEntity<List<NoteBookDto>> getNoteBookList(
        Authentication authentication
    ){
        User user = (User) authentication.getPrincipal();
        return null;
    }

    @GetMapping("/api/notebook/{notebookId}")
    public ResponseEntity<List<NoteDto>> getNoteBook(
            @PathVariable Long notebookId,
            Authentication authentication
    ){
        return null;
    }

    @DeleteMapping("/api/notebook/{notebookId}")
    public ResponseEntity<Void> deleteNoteBook(
            @PathVariable Long notebookId,
            Authentication authentication
    ){
        return null;
    }
}
