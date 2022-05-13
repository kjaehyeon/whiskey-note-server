package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookDto;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookResponse;
import com.jhkim.whiskeynote.api.service.NoteBookService;
import com.jhkim.whiskeynote.core.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notebook")
@Slf4j
public class NoteBookController {

    private final NoteBookService noteBookService;

    @PostMapping
    public ResponseEntity<Void> createNoteBook(
           @Valid @RequestBody NoteBookDto noteBookDto,
           User user
    ){

        noteBookService.createNoteBook(noteBookDto,user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{notebookId}")
    public ResponseEntity<Void> updateNoteBook(
            @Valid @RequestBody NoteBookDto noteBookDto,
            @PathVariable Long notebookId,
            User user
    ){
        noteBookService.updateNoteBook(notebookId,noteBookDto, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NoteBookResponse>> getNoteBooks(
        User user
    ){
        return new ResponseEntity<>(noteBookService.getNoteBooks(user), HttpStatus.OK);
    }

    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNoteBook(
            @PathVariable Long notebookId,
            User user
    ){
        noteBookService.deleteNoteBook(notebookId, user);
        return ResponseEntity.ok().build();
    }

    //TODO
    @GetMapping("/{notebookId}")
    public ResponseEntity<List<NoteDetailResponse>> getNoteBook(
            @PathVariable Long notebookId
    ){
        return new ResponseEntity<>(noteBookService.getNoteBook(notebookId), HttpStatus.OK);
    }
}
