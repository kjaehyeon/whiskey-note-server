package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;
import com.jhkim.whiskeynote.core.dto.UserDto;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("[유닛테스트] SERVICE - NOTEBOOK")
@ExtendWith(MockitoExtension.class)
class NoteBookServiceTest {

    @InjectMocks private NoteBookService sut;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private UserRepository userRepository;
    @Mock private NoteService noteService;
    @Mock private NoteRepository noteRepository;

    //노트북 정상생성
    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 정상적인 데이터 & 정상 생성")
    @Test
    void givenNormalNoteBook_whenCreateNoteBook_thenReturnOK(){
        //Given
        NoteBookCreateRequest noteBookCreateRequest = createNormalNoteBookCreateRequest("notebook 1");
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        NoteBook noteBook = noteBookCreateRequest.toEntity(user);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookByTitleAndWriter("notebook 1", user))
                .willReturn(Optional.empty());
        given(noteBookRepository.save(noteBook))
                .willReturn(noteBook);

        //When
        sut.createNoteBook(noteBookCreateRequest, userDto);

        //Then
        then(noteBookRepository).should().save(noteBook);
    }

    //노트북 생성시 이름 중복 되면 예외반환하기
    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 이름이 중복되면 예외반환")
    @Test
    void givenDuplicatedNoteBook_whenCreateNoteBook_ThenReturnException(){
        //Given
        NoteBookCreateRequest noteBookDto = createNormalNoteBookCreateRequest("notebook1");
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user))
                .willReturn(Optional.of(noteBookDto.toEntity(user)));

        //When
        Throwable throwable = catchThrowable(() -> sut.createNoteBook(noteBookDto, userDto));

        //Then
        then(noteBookRepository).should().findNoteBookByTitleAndWriter(noteBookDto.getTitle(), user);
        assertThat(throwable)
                .isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
    }

    //노트북 리스트 조회
    @DisplayName("[NOTEBOOK][GET] 노트북 목록 조회 - 해당 유저가 가진 노트북의 목록 조회")
    @Test
    void givenUser_whenGetNotebookList_thenReturnListOfNotebook(){
        //Given
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        List<NoteBookDetailResponse> noteBooks= List.of(
                NoteBookDetailResponse.of(1l,"notebook1",1,1,1,2),
                NoteBookDetailResponse.of(2l,"notebook2",1,1,1,0),
                NoteBookDetailResponse.of(3l, "notebook3",1,1,1,3)
        );

        given(noteBookRepository.findNoteBookAndNoteCntByWriterName(userDto.getUsername()))
                .willReturn(noteBooks);

        //When
        List<NoteBookDetailResponse> list = sut.getNoteBooks(userDto);

        //Then
        assertThat(list)
                .hasSize(3)
                .contains(noteBooks.get(0), Index.atIndex(0))
                .contains(noteBooks.get(1), Index.atIndex(1))
                .contains(noteBooks.get(2), Index.atIndex(2));
    }

    //노트북 정상 수정
    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 노트북 정상 수정")
    @Test
    void givenNormalNotebookDto_whenUpdateNotebook_thenReturnOk(){
        //Given
        Long notebook_id = 1L;
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        NoteBookCreateRequest noteBookDto = NoteBookCreateRequest.of("new notebook1", 1, 1, 1);
        NoteBook originalNotebook = createNoteBook("notebook1", user);
        NoteBook newNotebook = noteBookDto.toEntity(user);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookById(notebook_id))
                .willReturn(Optional.of(originalNotebook));
        given(noteBookRepository.findNoteBookByTitleAndWriter(newNotebook.getTitle(), user))
                .willReturn(Optional.empty());
        given(noteBookRepository.save(newNotebook))
                .willReturn(newNotebook);

        //When
        sut.updateNoteBook(notebook_id, noteBookDto, userDto);

        //Then
        then(noteBookRepository).should().findNoteBookById(notebook_id);
        then(noteBookRepository).should().findNoteBookByTitleAndWriter(newNotebook.getTitle(), user);
        then(noteBookRepository).should().save(newNotebook);
    }

    //존재하지 않는 노트북 수정시 예외반환
    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 존재하지 않는 노트북 수정시 예외 반환")
    @Test
    void givenNotExistNotebookId_whenUpdateNotebook_thenReturnException(){
        //Given
        Long notExistNotebookId = 1L;
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        NoteBookCreateRequest notExistNotebookDto = NoteBookCreateRequest.of("not exist notebook", 1, 1, 1);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookById(notExistNotebookId))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.updateNoteBook(notExistNotebookId, notExistNotebookDto, userDto));

        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    //노트북 수정시 기존 노트북들과 이름 중복되면 예외 반환
    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 수정한 이름이 기존 노트북과 중복되면 예외 반환")
    @Test
    void givenDuplicatedNotebook_whenUpdateNotebook_thenReturnException(){
        //Given
        Long notebook_id = 1L;
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        NoteBookCreateRequest duplicated_notebookDto = NoteBookCreateRequest.of("duplicated title", 1, 1, 1);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookByTitleAndWriter(duplicated_notebookDto.getTitle(), user))
                .willReturn(Optional.of(NoteBook.of("duplicated title", null,1, 1, 1)));

        //When
        Throwable throwable = catchThrowable(() -> sut.updateNoteBook(notebook_id, duplicated_notebookDto, userDto));

        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
    }

    //노트북 정상삭제
    @DisplayName("[NOTEBOOK][DELETE] 노트북 삭제 - 노트북 정상삭제")
    @Test
    void givenExistNoteBook_whenDeleteNoteBook_thenReturnOk(){
        //Given
        Long notebookId = 1L;
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);
        NoteBook noteBook = NoteBook.of("notebook1",user, 1,1,1);

        given(userRepository.findUserByUsername(userDto.getUsername()))
                .willReturn(Optional.of(user));
        given(noteBookRepository.findNoteBookById(notebookId))
                .willReturn(Optional.of(noteBook));
        given(noteRepository.findAllByNotebook(noteBook))
                .willReturn(
                        List.of(
                                createNote(1L, user, noteBook),
                                createNote(2L, user, noteBook)
                        )
                );

        //When
        sut.deleteNoteBook(1L, userDto);

        //Then
        then(noteBookRepository).should().delete(noteBook);
    }

    //존재하지 않는 노트북 삭제시 예외 반환
    @DisplayName("[NOTEBOOK][DELETE] 노트북 삭제 - 존재하지 않는 노트북 삭제")
    @Test
    void givenNotExistNotebook_whenDeleteNotebook_thenReturnException(){
        //Given
        Long notebookId = 1L;
        User user = createUser("user1");
        UserDto userDto = UserDto.fromEntity(user);

        given(noteBookRepository.findNoteBookById(notebookId))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.deleteNoteBook(notebookId, userDto));

        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    private Note createNote(
            Long noteId,
            User user,
            NoteBook noteBook
    ){
        Note note =  Note.of(
                            noteBook,
                            null,
                            user,
                            "wild turkey"
                            ,1F,null,1,1,null,null,null,null,null,1,
                            1,1,1,1,1,1,1,1,1,1,1
                    );
        note.setId(noteId);
        return note;
    }
    private User createUser(String username){
        return User.of(username ,"password1","user1@email.com","ROLE_USER", null);
    }
    private NoteBook createNoteBook(String title, User user){
        return NoteBook.of(title, user, 1,1,1);
    }
    private NoteBookCreateRequest createNormalNoteBookCreateRequest(String title){
        Random random = new Random();
        return NoteBookCreateRequest.of(
                title,
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
        );
    }
}