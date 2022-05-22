package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookDetailResponse;
import com.jhkim.whiskeynote.core.dto.UserDto;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void createNoteBook(
            NoteBookCreateRequest noteBookCreateRequest,
            UserDto userDto
    ){
        final User user = getUserFromUserDto(userDto);

        checkNotebookDuplication(noteBookCreateRequest, user);
        noteBookRepository.save(noteBookCreateRequest.toEntity(user));
    }

    //변경하려는 노트북의 이름이 중복되었는지 확인
    private void checkNotebookDuplication(NoteBookCreateRequest noteBookDto, User user){
        noteBookRepository.findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user)
                .ifPresent(e -> {
                    throw new GeneralException(ErrorCode.RESOURCE_ALREADY_EXISTS);
                });
    }

    private User getUserFromUserDto(UserDto userDto){
        return userRepository.findUserByUsername(userDto.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    //TODO 해당 노트북에 존재하는 노트 개수도 응답에 추가바람
    @Transactional
    public List<NoteBookDetailResponse> getNoteBooks(
            UserDto userDto
    ){
        return noteBookRepository.findNoteBookByWriter_Username(userDto.getUsername())
                .stream()
                .map(NoteBookDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateNoteBook(
            Long notebook_id,
            NoteBookCreateRequest noteBookCreateRequest,
            UserDto userDto
    ){
        final User user = getUserFromUserDto(userDto);

        //변경하려는 이름이 중복되는지 확인
        checkNotebookDuplication(noteBookCreateRequest, user);
        //변경할 노트북이 존재하는지 확인
        NoteBook noteBook = noteBookRepository.findNoteBookById(notebook_id)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        checkNotebookWriter(noteBook, user);

        noteBookRepository.save(noteBookCreateRequest.updateEntity(noteBook));
    }

    private void checkNotebookWriter(NoteBook noteBook, User user){
        //삭제하려는 유저가 작성자가 아니면 FORBIDDEN
        if(!noteBook.getWriter().getUsername().equals(user.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
    }

    @Transactional
    public void deleteNoteBook(
            Long notebookId,
            UserDto userDto
    ){
       final NoteBook noteBook = noteBookRepository.findNoteBookById(notebookId)
               .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
       final User user = getUserFromUserDto(userDto);
       checkNotebookWriter(noteBook, user);

       noteBookRepository.delete(noteBook);
    }
}
