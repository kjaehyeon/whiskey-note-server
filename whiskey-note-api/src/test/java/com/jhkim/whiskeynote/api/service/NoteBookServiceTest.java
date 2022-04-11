package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.NoteBookDto;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("[유닛테스트] SERVICE - NoteBook 서비스")
@ExtendWith(MockitoExtension.class)
class NoteBookServiceTest {

    @InjectMocks private NoteBookService sut;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private UserRepository userRepository;

    private NoteBookDto createNormalNoteBookDto(String title){
        Random random = new Random();
        return NoteBookDto.of(
                title,
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
        );
    }

    //노트북 정상생성
    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 정상적인 데이터 & 정상 생성")
    @Test
    void givenNormalNoteBook_whenCreateNoteBook_thenReturnOK(){
        //Given
        NoteBookDto noteBookDto = createNormalNoteBookDto("notebook 1");
        NoteBook noteBook = noteBookDto.toEntity();

        given(noteBookRepository.save(noteBook)).willReturn(noteBook);
        //When
        sut.create(noteBookDto);

        //Then
        then(noteBookRepository).should().save(noteBook);
    }

    //노트북 생성시 이름 중복 되면 예외반환하기
    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 이름이 중복되면 예외반환")
    @Test
    void givenDuplicatedNoteBook_whenCreateNoteBook_ThenReturnException(){
        //Given
        NoteBookDto noteBookDto = createNormalNoteBookDto("notebook 1");
        given(noteBookRepository.findNoteBookByWriter(any(User.class)))
                .willReturn(List.of(
                   NoteBook.of("notebook 1", null,1, 1,1),
                    NoteBook.of("notebook 2", null,1, 1,1)
                ));
        //When
        Throwable throwable = catchThrowable(() -> sut.create(noteBookDto));
        //Then
        assertThat(throwable)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.RESOURCE_ALREADY_EXISTS.getMessage());
        then(noteBookRepository).shouldHaveNoInteractions();
    }

    //노트북 생성시 이름이 비어있으면 예외반환 - 클라이언트 단에서 처리할 수 있을 듯
    /**
     * 이 아래는 다 권한확인해서 관리자, 주인만 가능한 기능
     */

    //노트북 정상삭제
    @DisplayName("[NOTEBOOK][DELETE] 노트북 삭제 - 노트북 정상삭제")
    @Test
    void givenExistNoteBook_whenDeleteNoteBook_thenReturnOk(){
        //Given
        Long notebookId = 1L;
        willDoNothing().given(noteBookRepository).deleteById(notebookId);
        //When
        sut.delete(1L);
        //Then
        then(noteBookRepository).should().deleteById(notebookId);
    }

    //존재하지 않는 노트북 삭제시 예외 반환 - 예외 반환할 필요 없을 듯

    //노트북 정상 수정
    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 노트북 정상 수정")
    @Test
    void givenNormalNotebookDto_whenUpdateNotebook_thenReturnOk(){
        //Given
        Long notebook_id = 1L;
        NoteBook originalNotebook = NoteBook.of("notebook1", null, 1, 1, 1);
        NoteBook changedNotebook = NoteBook.of("new notebook1", null,1, 1, 1);

        NoteBookDto noteBookDto = NoteBookDto.of("new notebook1", 1, 1, 1);

        given(noteBookRepository.findNoteBookById(originalNotebook.getId())).willReturn(Optional.of(originalNotebook));
        given(noteBookRepository.save(changedNotebook)).willReturn(changedNotebook);

        //When
        sut.upsert(notebook_id, noteBookDto);

        //Then
        then(noteBookRepository).should().findNoteBookById(changedNotebook.getId());
        then(noteBookRepository).should().save(changedNotebook);
    }

    //존재하지 않는 노트북 수정시 예외반환
    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 존재하지 않는 노트북 수정시 예외 반환")
    @Test
    void givenNotExistNotebookId_whenUpdateNotebook_thenReturnException(){
        //Given
        Long notExistNotebookId = 1L;
        NoteBook notExistNotebook = NoteBook.of("not exist notebook", null, 1, 1, 1);
        NoteBookDto notExistNotebookDto = NoteBookDto.of("not exist notebook", 1, 1, 1);

        given(noteBookRepository.findNoteBookById(any())).willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.upsert(notExistNotebookId, notExistNotebookDto));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.RESOURCE_NOT_FOUND.getMessage());
    }

    //노트북 수정시 기존 노트북들과 이름 중복되면 예외 반환

    //노트북 수정시 이름 비어있으면 예외 반환 - 클라이언트 단에서 처리할 수 있을 듯

    //단일 노트북 정상 조회 - 해당 노트북에 속한 노트의 리스트 반환

    //존재하지 않는 노트북 단일 조회시 예외 반환

    //노트북 리스트 조회
}