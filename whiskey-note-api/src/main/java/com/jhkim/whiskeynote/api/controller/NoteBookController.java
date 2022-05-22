package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookDetailResponse;
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
           @Valid @RequestBody NoteBookCreateRequest noteBookDto,
           UserDto userDto
    ){

        noteBookService.createNoteBook(noteBookDto, userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NoteBookDetailResponse>> getNoteBooks(
            UserDto userDto
    ){
        return new ResponseEntity<>(noteBookService.getNoteBooks(userDto), HttpStatus.OK);
    }

    @PutMapping("/{notebookId}")
    public ResponseEntity<Void> updateNoteBook(
            @Valid @RequestBody NoteBookCreateRequest noteBookDto,
            @PathVariable Long notebookId,
            UserDto userDto
    ){
        noteBookService.updateNoteBook(notebookId,noteBookDto, userDto);

        return ResponseEntity.ok().build();
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
