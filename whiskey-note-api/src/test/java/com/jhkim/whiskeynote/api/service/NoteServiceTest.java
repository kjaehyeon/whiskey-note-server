package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.core.constant.*;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
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

@DisplayName("[유닛테스트] SERVICE - Note 서비스")
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @InjectMocks private NoteService sut;
    @Mock private NoteRepository noteRepository;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private NoteImageRepository noteImageRepository;
    @Mock private WhiskeyRepository whiskeyRepository;
    @Mock private AwsS3Service awsS3Service;

    /**
     * createNote()
     */
    //노트 정상 생성 (이미지 함께)
    @Test
    void givenNormalNoteCreateRequest_whenCreateNote_thenReturnOk(){
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
        List<String> urls = List.of("url1", "url1");
        given(whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId()))
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));
        given(awsS3Service.uploadImages(
                noteCreateRequest.getImages(),
                S3Path.NOTE_IMAGE.getFolderName())).willReturn(urls);

        //When
        sut.createNote(noteCreateRequest, user);

        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(whiskey, noteBook));
        then(noteImageRepository).should(times(urls.size())).save(any());
    }

    //노트 정상 생성 (이미지 없이)
    @Test
    void givenNormalNoteWithNoImage_whenCreateNote_thenReturnOK(){
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
                .willReturn(Optional.of(whiskey));
        given(noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId()))
                .willReturn(Optional.of(noteBook));

        //When
        sut.createNote(noteCreateRequest, user);
        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(whiskey, noteBook));
        then(noteImageRepository).shouldHaveNoInteractions();
    }
    //참조하는 위스키 없이 노트 정상 생성
    @Test
    void givenNormalNoteWithNoWhiskey_whenCreateNote_thenReturnOK(){
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
                .willReturn(Optional.of(noteBook));

        //When
        sut.createNote(noteCreateRequest, user);
        //Then
        then(noteRepository).should().save(noteCreateRequest.toEntity(whiskey, noteBook));
    }
    //존재하지 않는 위스키로 생성요청
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
    //존재하지 않는 노트북에 생성요청
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
    //노트 정상 삭제(이미지 없는경우)
    @Test
    void givenNoteId_whenDeleteNote_thenOK(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(createNormalNote(user)));
        given(noteImageRepository.findNoteImageByNote_Id(noteId)).willReturn(List.of());
        //When
        sut.deleteNote(noteId, user);
        //Then
        then(awsS3Service).shouldHaveNoInteractions();
        then(noteRepository).should().deleteNoteById(noteId);
    }
    //노트 정상 삭제(이미지 있는 경우)
    @Test
    void givenNoteIdWithImages_whenDeleteNote_thenOK(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        Note note = createNormalNote(user);
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
    }
    //삭제 요청한 유저가 노트 작성자가 아닌 경우
    @Test
    void givenNotWriterOfNote_whenDeleteNote_thenException(){
        //Given
        User noteOwner = createUser("user1");
        User anotherUser = createUser("user2");
        Long noteId = 1l;
        Note note = createNormalNote(noteOwner);
        given(noteRepository.findNoteById(noteId))
                .willReturn(Optional.of(note));
        //When
        Throwable throwable = catchThrowable(() -> sut.deleteNote(noteId, anotherUser));
        //Then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(((GeneralException)throwable)
                .getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }
    //존재하지 않는 노트인 경우
    @Test
    void givenNotExistNoteId_whenDeleteNote_thenException(){
        //Given
        User user = createUser("user1");
        Long noteId = 1l;
        Note note = createNormalNote(user);
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
    //노트 정상 수정
    //수정 요청한 유저가 노트 작성자가 아닌경우
    //수정 요청에 이미지가 비어 있는 경우
    //수정할 노트가 존재하지 않는 경우
    //whiskey color가 존재하지 않는 Enum인 경우
    //존재하지 않는 위스키로 수정요청
    //존재하지 않는 노트북으로 수정요청'

    /**
     * getNote()
     */
    //노트 정상 조회
    //존재하지 않는 노트 조회하는 경우

    /**
     * getNotes()
     */
    //노트 목록 정상 조회
    //존재하지 않는 노트북의 노트목록을 조회하는 경우

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
            User user
    ){
        return Note.builder()
                .notebook(createNoteBook("title1", user))
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
