package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.NoteBookDto;
import com.jhkim.whiskeynote.api.dto.NoteBookResponse;
import com.jhkim.whiskeynote.api.dto.NoteDto;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
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
import java.util.Optional;
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

        notebookDuplicationCheck(noteBookDto, user);
        noteBookRepository.findNoteBookById(notebook_id)
                .ifPresent(e ->{
                    //수정하려는 유저가 작성자가 아니면 FORBIDDEN
                   if(!e.getWriter().getUsername().equals(user.getUsername())){
                       throw new GeneralException(ErrorCode.FORBIDDEN);
                   }
                   noteBookRepository.save(noteBookDto.updateEntity(e));
                });
    }

    @Transactional
    public void delete(
            Long notebook_id,
            User user
    ){
        log.info("================in delete");

       final NoteBook noteBook =  noteBookRepository.findNoteBookById(notebook_id)
               .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

       if(!noteBook.getWriter().getUsername().equals(user.getUsername())){
           throw new GeneralException(ErrorCode.FORBIDDEN);
       }

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
    public List<NoteDto> getNoteBook(
            Long notebook_id
    ){
        return null;
    }

    private void notebookDuplicationCheck(NoteBookDto noteBookDto, User user){
        noteBookRepository.findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user)
                .ifPresent(e -> {
                    throw new GeneralException(ErrorCode.RESOURCE_ALREADY_EXISTS);
                });
    }
}
