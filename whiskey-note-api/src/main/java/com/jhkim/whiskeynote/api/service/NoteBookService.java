package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookDetailResponse;
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
    public void createNoteBook(
            NoteBookCreateRequest noteBookCreateRequest,
            User user
    ){
        checkNotebookDuplication(noteBookCreateRequest, user);
        noteBookRepository.save(noteBookCreateRequest.toEntity(user));
    }

    @Transactional
    public void updateNoteBook(
            Long notebook_id,
            NoteBookCreateRequest noteBookCreateRequest,
            User user
    ){
        //변경하려는 이름이 중복되는지 확인
        checkNotebookDuplication(noteBookCreateRequest, user);
        //변경할 노트북이 존재하는지 확인
        NoteBook noteBook = noteBookRepository.findNoteBookById(notebook_id)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        checkNotebookWriter(noteBook, user);

        noteBookRepository.save(noteBookCreateRequest.updateEntity(noteBook));
    }

    @Transactional
    public void deleteNoteBook(
            Long notebookId,
            User user
    ){
       final NoteBook noteBook = noteBookRepository.findNoteBookById(notebookId)
               .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

       checkNotebookWriter(noteBook, user);

       noteBookRepository.delete(noteBook);
    }

    //TODO 해당 노트북에 존재하는 노트 개수도 응답에 추가바람
    @Transactional
    public List<NoteBookDetailResponse> getNoteBooks(
            User user
    ){
        return noteBookRepository.findNoteBookByWriter(user)
                .stream()
                .map(NoteBookDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //변경하려는 노트북의 이름이 중복되었는지 확인
    private void checkNotebookDuplication(NoteBookCreateRequest noteBookDto, User user){
        noteBookRepository.findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user)
                .ifPresent(e -> {
                    throw new GeneralException(ErrorCode.RESOURCE_ALREADY_EXISTS);
                });
    }

    private void checkNotebookWriter(NoteBook noteBook, User user){
        //삭제하려는 유저가 작성자가 아니면 FORBIDDEN
        if(!noteBook.getWriter().getUsername().equals(user.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
    }
}
