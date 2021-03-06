package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookUpdateResponse;
import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;
import com.jhkim.whiskeynote.api.service.NoteBookService;
import com.jhkim.whiskeynote.core.dto.UserDto;
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
           @Valid @RequestBody NoteBookCreateRequest noteBookCreateRequest,
           UserDto userDto
    ){

        noteBookService.createNoteBook(noteBookCreateRequest, userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NoteBookDetailResponse>> getNoteBooks(
            UserDto userDto
    ){
        return new ResponseEntity<>(noteBookService.getNoteBooks(userDto), HttpStatus.OK);
    }

    @PutMapping("/{notebookId}")
    public ResponseEntity<NoteBookUpdateResponse> updateNoteBook(
            @Valid @RequestBody NoteBookCreateRequest noteBookCreateRequest,
            @PathVariable Long notebookId,
            UserDto userDto
    ){
        return new ResponseEntity<>(
                noteBookService.updateNoteBook(notebookId, noteBookCreateRequest, userDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNoteBook(
            @PathVariable Long notebookId,
            UserDto userDto
    ){
        noteBookService.deleteNoteBook(notebookId, userDto);
        return ResponseEntity.ok().build();
    }

}
