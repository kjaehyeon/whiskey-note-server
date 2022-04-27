package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookDto;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookResponse;
import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteBookService {
    private final NoteBookRepository noteBookRepository;

    @Transactional
    public void create(
            NoteBookDto noteBookDto,
            User user
    ){
        notebookDuplicationCheck(noteBookDto, user);
        noteBookRepository.save(noteBookDto.toEntity(user));
    }

    @Transactional
    public void upsert(
            Long notebook_id,
            NoteBookDto noteBookDto,
            User user
    ){
        //변경하려는 이름이 중복되는지 확인
        notebookDuplicationCheck(noteBookDto, user);

        //변경할 노트북이 존재하는지 확인
        NoteBook noteBook = noteBookRepository.findNoteBookById(notebook_id)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        notebookWriterCheck(noteBook, user);

        noteBookRepository.save(noteBookDto.updateEntity(noteBook));
    }

    @Transactional
    public void delete(
            Long notebook_id,
            User user
    ){
       final NoteBook noteBook = noteBookRepository.findNoteBookById(notebook_id)
               .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

       notebookWriterCheck(noteBook, user);

       noteBookRepository.delete(noteBook);
    }

    @Transactional
    public List<NoteBookResponse> getNoteBookList(
            User user
    ){
        return noteBookRepository.findNoteBookByWriter(user)
                .stream()
                .map(NoteBookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //TODO
    @Transactional
    public List<NoteDetailResponse> getNoteBook(
            Long notebook_id
    ){
        return null;
    }

    //변경하려는 노트북의 이름이 중복되었는지 확인
    private void notebookDuplicationCheck(NoteBookDto noteBookDto, User user){
        noteBookRepository.findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user)
                .ifPresent(e -> {
                    throw new GeneralException(ErrorCode.RESOURCE_ALREADY_EXISTS);
                });
    }

    private void notebookWriterCheck(NoteBook noteBook, User user){
        //삭제하려는 유저가 작성자가 아니면 FORBIDDEN
        if(!noteBook.getWriter().getUsername().equals(user.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
    }
}
