package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.core.constant.*;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.repository.querydsl.NoteImageRepositoryCustom;
import com.jhkim.whiskeynote.core.repository.querydsl.NoteRepositoryCustom;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[유닛테스트] SERVICE - NOTE")
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @InjectMocks private NoteService sut;
    @Mock private NoteRepository noteRepository;
    @Mock private NoteRepositoryCustom noteRepositoryCustom;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private NoteImageRepository noteImageRepository;
    @Mock private NoteImageRepositoryCustom noteImageRepositoryCustom;
    @Mock private WhiskeyRepository whiskeyRepository;
    @Mock private AwsS3Service awsS3Service;

    /**
     * createNote()
     */
    @DisplayName("[NOTE][CREATE] 노트 생성 - 노트 정상 생성(이미지 있음)")
    @Test
    void givenNormalNoteCreateRequest_whenCreateNote_thenReturnNoteDetailResponse(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        NoteCreateRequest noteCreateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        List<String> urls = List.of("url1", "url2");
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));
        given(noteRepository.save(noteCreateRequest.toEntity(user,whiskey, noteBook)))
                .willReturn(noteCreateRequest.toEntity(user, whiskey, noteBook));
        given(awsS3Service.uploadImages(
                noteCreateRequest.getImages(),
                S3Path.NOTE_IMAGE.getFolderName())).willReturn(urls);

        //When
        NoteDetailResponse createdNote = sut.createNote(noteCreateRequest, user);

        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        then(noteImageRepository).should(times(urls.size())).save(any());
        assertThat(createdNote).isEqualTo(NoteDetailResponse.fromEntity(
                noteCreateRequest.toEntity(user, whiskey, noteBook),
                urls
        ));
    }

    @DisplayName("[NOTE][CREATE] 노트 생성 - 노트 정상 생성 (이미지 없음)")
    @Test
    void givenNormalNoteWithNoImage_whenCreateNote_thenReturnNoteDetailResponse(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        NoteCreateRequest noteCreateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(0)
                );
        List<String> urls = List.of();
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));
        given(noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook)))
                .willReturn(noteCreateRequest.toEntity(user, whiskey, noteBook));

        //When
        NoteDetailResponse createdNote = sut.createNote(noteCreateRequest, user);
        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        then(noteImageRepository).shouldHaveNoInteractions();
        assertThat(createdNote).isEqualTo(NoteDetailResponse.fromEntity(
                noteCreateRequest.toEntity(user, whiskey, noteBook),
                urls
        ));
    }

    @DisplayName("[NOTE][CREATE] 노트 생성 - 노트 정상 생성(참조하는 위스키 없음)")
    @Test
    void givenNormalNoteWithNoWhiskey_whenCreateNote_thenReturnNoteDetailResponse(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        NoteCreateRequest noteCreateRequest =
                createNoteCreateRequestWithNoWhiskeyId(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(0)
                );
        List<String> urls = List.of();
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));
        given(noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook)))
                .willReturn(noteCreateRequest.toEntity(user, whiskey, noteBook));

        //When
        NoteDetailResponse createdNote = sut.createNote(noteCreateRequest, user);
        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        assertThat(createdNote).isEqualTo(NoteDetailResponse.fromEntity(
                noteCreateRequest.toEntity(user, whiskey, noteBook),
                urls
        ));
    }

    @DisplayName("[NOTE][CREATE] 노트 생성 - 존재하지 않는 위스키ID로 노트 생성 요청")
    @Test
    void givenNotExistWhiskey_whenCreateNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        NoteCreateRequest noteCreateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(0)
                );
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.createNote(noteCreateRequest, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @DisplayName("[NOTE][CREATE] 노트 생성 - 존재하지 않는 노트북ID에 생성요청")
    @Test
    void givenNotExistNotebook_whenCreateNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        NoteCreateRequest noteCreateRequest =
                createNoteCreateRequestWithNoWhiskeyId(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(0)
                );
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.createNote(noteCreateRequest, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);

    }

    /**
     * deleteNote()
     */
    @DisplayName("[NOTE][DELETE] 노트 삭제 - 노트 정상 삭제(이미지 없음)")
    @Test
    void givenNoteId_whenDeleteNote_thenReturnOK(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);

        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(createNormalNote(user, whiskey, noteBook)));
        given(noteImageRepository.findNoteImageByNote_Id(noteId)).willReturn(List.of());
        //When
        sut.deleteNote(noteId, user);
        //Then
        then(awsS3Service).shouldHaveNoInteractions();
        then(noteRepository).should().deleteNoteById(noteId);
    }
    @DisplayName("[NOTE][DELETE] 노트 삭제 - 노트 정상 삭졔(이미지 있음)")
    @Test
    void givenNoteIdWithImages_whenDeleteNote_thenReturnOK(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Note note = createNormalNote(user, whiskey, noteBook);
        List<NoteImage> noteImages = List.of(
                NoteImage.of(note, "url1"),
                NoteImage.of(note, "url2")
                );
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(note));
        given(noteImageRepository.findNoteImageByNote_Id(noteId))
                .willReturn(noteImages);
        //When
        sut.deleteNote(noteId, user);
        //Then
        then(awsS3Service).should(times(noteImages.size())).deleteImage(any());
        then(noteRepository).should().deleteNoteById(noteId);
        then(noteImageRepositoryCustom).should().deleteAllByNote_id(noteId);
    }

    @DisplayName("[NOTE][DELETE] 노트 삭제 - 노트 작성자가 아닌 유저가 노트 삭제 요청")
    @Test
    void givenNotWriterOfNote_whenDeleteNote_thenReturnException(){
        //Given
        User noteOwner = createUser("user1");
        User anotherUser = createUser("user2");
        Long noteId = 1l;
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",noteOwner);
        Note note = createNormalNote(noteOwner, whiskey, noteBook);
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(note));
        //When
        Throwable throwable = catchThrowable(() -> sut.deleteNote(noteId, anotherUser));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }
    @DisplayName("[NOTE][DELETE] 노트 삭제 - 존재하지 않는 노트 삭제 요청")
    @Test
    void givenNotExistNoteId_whenDeleteNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Note note = createNormalNote(user, whiskey, noteBook);

        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> sut.deleteNote(noteId, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);

    }

    /**
     * updateNote()
     */
    @DisplayName("[NOTE][PUT] 노트 수정 - 노트 정상 수정(이미지 있음)")
    @Test
    void givenNormalUpdateRequest_whenUpdateNote_thenReturnNoteDetailResponse(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Long noteId = 1l;
        Note note = createNormalNote(user, whiskey, noteBook);
        NoteCreateRequest noteUpdateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        List<String> urls = List.of("url1", "url2");

        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(note));
        given(whiskeyRepository.findWhiskeyById(whiskey.getId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteUpdateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));
        given(awsS3Service.uploadImages(noteUpdateRequest.getImages(), S3Path.NOTE_IMAGE.getFolderName()))
                .willReturn(urls);

        //When
        NoteDetailResponse result = sut.updateNote(noteId, noteUpdateRequest, user);
        //Then
        assertThat(result).isEqualTo(NoteDetailResponse
                .fromEntity(noteUpdateRequest.toEntity(user, whiskey, noteBook), urls));

    }

    @DisplayName("[NOTE][PUT] 노트 수정 - 노트 정상 수정(이미지 있음)")
    @Test
    void givenNotWriterOfNote_whenUpdateNote_thenReturnException(){
        //Given
        User owner = createUser("user1");
        User anotherUser = createUser("user2");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1", owner);
        Long noteId = 1l;
        NoteCreateRequest noteUpdateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(
                        noteUpdateRequest.toEntity(owner, whiskey, noteBook))
                );
        //When
        Throwable throwable = catchThrowable(() -> sut.updateNote(noteId, noteUpdateRequest, anotherUser));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.FORBIDDEN);

    }

    @DisplayName("[NOTE][PUT] 노트 수정 - 수정할 노트가 존재하지 않음")
    @Test
    void givenNotExistNote_whenUpdateNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Long noteId = 1l;
        NoteCreateRequest noteUpdateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> sut.updateNote(noteId, noteUpdateRequest, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }


    @DisplayName("[NOTE][PUT] 노트 수정 - 존재하지 않는 위스키로 수정요청")
    @Test
    void givenNotExistWhiskey_whenUpdateNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Long noteId = 1l;
        NoteCreateRequest noteUpdateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(noteUpdateRequest.toEntity(user, whiskey, noteBook)));
        given(whiskeyRepository.findWhiskeyById(noteUpdateRequest.getWhiskeyId()))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> sut.updateNote(noteId, noteUpdateRequest, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @DisplayName("[NOTE][PUT] 노트 수정 - 존재하지 않는 노트북으로 수정요청")
    @Test
    void givenNotExistNotebook_whenUpdateNote_thenReturnException(){
        //Given
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Long noteId = 1l;
        NoteCreateRequest noteUpdateRequest =
                createNormalNoteCreateRequest(
                        whiskey,
                        noteBook,
                        WhiskeyColor.AMBER.name(),
                        createMultiFiles(2)
                );
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(noteUpdateRequest.toEntity(user, whiskey, noteBook)));
        given(whiskeyRepository.findWhiskeyById(noteUpdateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteBook.getId()))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> sut.updateNote(noteId, noteUpdateRequest, user));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }
    /**
     * getNote()
     */
    @DisplayName("[NOTE][GET] 노트 단일 조회 - 정상 조회")
    @Test
    void givenNoteId_whenGetNote_thenReturnNoteDetailResponse(){
        //Given
        Long noteId = 1l;
        User user = createUser("user1");
        List<String> urls = List.of("url1", "url2");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Note note = createNormalNote(user, whiskey, noteBook);

        given(noteRepositoryCustom.findNoteById(noteId))
                .willReturn(Optional.of(note));
        given(noteImageRepository.findNoteImageByNote_Id(noteId))
                .willReturn(List.of(NoteImage.of(note, urls.get(0)), NoteImage.of(note, urls.get(1))));
        //When
        NoteDetailResponse result = sut.getNote(noteId);
        //Then
        assertThat(result)
                .isEqualTo(NoteDetailResponse.fromEntity(createNormalNote(user, whiskey, noteBook), urls));
    }
    @DisplayName("[NOTE][GET] 노트 단일 조회 - 존재하지 않는 노트 조회")
    @Test
    void givenNoteExistNoteId_whenGetNote_thenReturnException(){
        //Given
        Long noteId = 1l;
        given(noteRepositoryCustom.findNoteById(noteId))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> sut.getNote(noteId));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }
    /**
     * getNotes()
     */
    @DisplayName("[NOTE][GET] 노트 목록 조회 - 정상 조회")
    @Test
    void givenNotebookId_whenGetNotes_thenReturnNoteDetailResponses(){
        //Given
        Long notebookId = 1l;
        User user = createUser("user1");
        Whiskey whiskey = createWhiskey("Balvenie","Double Wood");
        NoteBook noteBook = createNoteBook("note1",user);
        Note note1 = createNormalNote(user, whiskey, noteBook);
        note1.setId(1l);
        Note note2 = createNormalNote(user, whiskey, noteBook);
        note2.setId(2l);
        List<String> note1Urls = List.of("note1url1", "note1url2");
        List<String> note2Urls = List.of("note2url1", "note2url2");

        given(noteBookRepository.findNoteBookById(notebookId))
                .willReturn(Optional.of(noteBook));
        given(noteRepository.findAllByNotebook(noteBook))
                .willReturn(List.of(note1, note2));
        given(noteImageRepository.findNoteImageByNote_Id(note1.getId()))
                .willReturn(List.of(
                        NoteImage.of(note1, note1Urls.get(0)),
                        NoteImage.of(note1, note1Urls.get(1)
                        )
                ));
        given(noteImageRepository.findNoteImageByNote_Id(note2.getId()))
                .willReturn(List.of(
                        NoteImage.of(note2, note2Urls.get(0)),
                        NoteImage.of(note2, note2Urls.get(1)
                        )
                ));
        //When
        List<NoteDetailResponse> result = sut.getNotes(notebookId);
        //Then
        assertThat(result)
                .hasSize(2)
                .isNotEmpty()
                .contains(NoteDetailResponse.fromEntity(note1,note1Urls), Index.atIndex(0))
                .contains(NoteDetailResponse.fromEntity(note2,note2Urls), Index.atIndex(1));
    }
    @DisplayName("[NOTE][GET] 노트 목록 조회 - 존재하지 않는 노트북ID 조회")
    @Test
    void givenNotExistNotebookId_whenGetNotes_thenReturnException(){
        //Given
        Long notebookId = 1l;
        given(noteBookRepository.findNoteBookById(notebookId))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> sut.getNotes(notebookId));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable).getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    private NoteCreateRequest createNormalNoteCreateRequest(
            Whiskey whiskey,
            NoteBook noteBook,
            String whiskeyColorString,
            List<MultipartFile> multipartFiles
    ){
        return NoteCreateRequest.builder()
                .whiskeyId(whiskey.getId())
                .notebookId(noteBook.getId())
                .whiskeyName(whiskey.getName())
                .distiller("Macallan")
                .price(99900)
                .rating(4.0f)
                .age(12)
                .nose("nose")
                .taste("taste")
                .finish("finish")
                .description("description")
                .color(whiskeyColorString)
                .smokey(1)
                .peaty(2)
                .herbal(3)
                .briny(1)
                .vanilla(2)
                .fruity(3)
                .floral(1)
                .woody(2)
                .rich(3)
                .spicy(1)
                .sweet(2)
                .salty(3)
                .images(multipartFiles)
                .build();
    }
    private NoteCreateRequest createNoteCreateRequestWithNoWhiskeyId(
            Whiskey whiskey,
            NoteBook noteBook,
            String whiskeyColorString,
            List<MultipartFile> multipartFiles
    ){
        return NoteCreateRequest.builder()
                .whiskeyId(null)
                .notebookId(noteBook.getId())
                .whiskeyName(whiskey.getName())
                .distiller("Macallan")
                .price(99900)
                .rating(4.0f)
                .age(12)
                .nose("nose")
                .taste("taste")
                .finish("finish")
                .description("description")
                .color(whiskeyColorString)
                .smokey(1)
                .peaty(2)
                .herbal(3)
                .briny(1)
                .vanilla(2)
                .fruity(3)
                .floral(1)
                .woody(2)
                .rich(3)
                .spicy(1)
                .sweet(2)
                .salty(3)
                .images(multipartFiles)
                .build();
    }
    private User createUser(
            String username
    ){
        return User.of(username ,"password1","user1@email.com","ROLE_USER", null);
    }
    private Whiskey createWhiskey(
            String brand,
            String name
    ){
        return Whiskey.builder()
                .brand(brand)
                .name(name)
                .distillery("Macallan")
                .category(WhiskeyCategory.SINGLE_MALT)
                .district(WhiskeyDistrict.SPEYSIDE)
                .bottler("증류소 보틀링")
                .statedAge(12)
                .vintage(null)
                .caskType("sherry cask")
                .alc(40.0f)
                .retailPrice(100000)
                .size(700)
                .bottledFor("bottledFor")
                .isColored(Bool3.valueOf("YES"))
                .isChillfiltered(Bool3.valueOf("YES"))
                .isSingleCask(Bool3.valueOf("YES"))
                .isCaskStrength(Bool3.valueOf("YES"))
                .isSmallBatch(Bool3.valueOf("YES"))
                .build();
    }
    private NoteBook createNoteBook(
            String title,
            User user
    ){
        return NoteBook.of(title, user, 1,1,1);
    }
    private Note createNormalNote(
            User user,
            Whiskey whiskey,
            NoteBook noteBook
    ){
        return Note.builder()
                .writer(user)
                .notebook(noteBook)
                .whiskey(whiskey)
                .whiskeyName(whiskey.getName())
                .distiller("Macallan")
                .price(99900)
                .rating(4.0f)
                .age(12)
                .nose("nose")
                .taste("taste")
                .finish("finish")
                .description("description")
                .color(WhiskeyColor.AMBER)
                .smokey(1)
                .peaty(2)
                .herbal(3)
                .briny(1)
                .vanilla(2)
                .fruity(3)
                .floral(1)
                .woody(2)
                .rich(3)
                .spicy(1)
                .sweet(2)
                .salty(3)
                .build();
    }
    private List<MultipartFile> createMultiFiles(Integer num){
        List<MultipartFile> multipartFiles = new ArrayList<>();
        final String fileName = "testImage";
        final String contentType = "png";
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType;
        for(int i = 0; i < num; i++){
            multipartFiles.add(getMockMultipartFile(fileName,contentType, filePath));
        }
        return multipartFiles;
    }
    private MockMultipartFile getMockMultipartFile(
            String fileName,
            String contentType,
            String path
    ) {
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream(path);
            return new MockMultipartFile(
                    fileName,
                    fileName + "." + contentType,
                    contentType,
                    fileInputStream);
        }catch (IOException io){
        }
        return null;
    }

}
