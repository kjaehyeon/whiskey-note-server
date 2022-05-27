package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.api.jwt.JwtProperties;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.repository.*;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import com.jhkim.whiskeynote.core.service.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[통합테스트] CONTROLLER - NOTE")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class NoteControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private UserRepository userRepository;
    @Autowired private DatabaseCleanup databaseCleanup;
    @Autowired private NoteBookRepository noteBookRepository;
    @Autowired private WhiskeyRepository whiskeyRepository;
    @Autowired private NoteRepository noteRepository;
    @Autowired private NoteImageRepository noteImageRepository;
    @Autowired private JwtUtils jwtUtils;

    //aws s3 upload 부분은 mocking
    @MockBean private AwsS3Service awsS3Service;

    private User user;
    private String token;
    private NoteBook noteBook;
    private Whiskey whiskey;

    @BeforeEach
    void setUp(){
        databaseCleanup.execute();
        user = createUser("user1");
        noteBook = createNoteBook("notebook1", user);
        token = jwtUtils.createToken(user);
        whiskey = createWhiskey("Macallan", "Macallan enigma");
        userRepository.save(user);
        noteBookRepository.save(noteBook);
        whiskeyRepository.save(whiskey);
    }

    @DisplayName("[NOTE][POST] 노트 정상 생성")
    @Test
    void givenNormalNoteCreateRequest_whenCreateNote_thenOk() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        Note noteToSave = noteCreateRequest.toEntity(user, whiskey, noteBook);
        noteToSave.setId(1l);

        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                createMultiPartRequest(multipartRequest, noteCreateRequest, images)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        mapper.writeValueAsString(
                                NoteDetailResponse.fromEntity(noteToSave, imageUrls)
                        )
                ));

        assertThat(noteRepository.findAllByWriter(user))
                .contains(noteToSave);
        assertThat(noteImageRepository.findAll().stream().map(NoteImage::getUrl))
                .containsAll(imageUrls);
    }
    private MockHttpServletRequestBuilder createMultiPartRequest(
            MockMultipartHttpServletRequestBuilder multipartRequest,
            NoteCreateRequest noteCreateRequest,
            List<MockMultipartFile> images
    ){
        for(MockMultipartFile image : images){
            multipartRequest.file(image);
        }
        final PropertyDescriptor[] getterDescriptors = ReflectUtils.getBeanGetters(NoteCreateRequest.class);
        for(PropertyDescriptor pd : getterDescriptors){
            try{
                String value = String.valueOf(pd.getReadMethod().invoke(noteCreateRequest));
                if(!value.equals("null")){
                    multipartRequest.param(pd.getName(), value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return multipartRequest
                .header(JwtProperties.KEY_NAME, token);
    }
    private List<String> createImageUrls(int numberOfImage){
        List<String> imageUrls = new ArrayList<>();
        for(int i = 0; i < numberOfImage; i++){
            imageUrls.add("imageUrl" + i);
        }

        return imageUrls;
    }

    @DisplayName("[NOTE][POST] 노트 생성시 노트북ID가 null이면 예외")
    @Test
    void givenNullNotebookIdNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setNotebookId(null);
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteCreateRequest, images)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));

    }

    @DisplayName("[NOTE][POST] 노트 생성시 위스키 이름이 빈칸이면 예외")
    @Test
    void givenBlankWhiskeyNameNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setWhiskeyName("");
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteCreateRequest, images)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTE][POST] 노트 생성시 평점이 범위 초과면 예외")
    @Test
    void givenInvalidRatingNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setRating(5.1f);
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteCreateRequest, images)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTE][POST] 노트 생성시 향기 값들이 범위 초과면 예외 (1)")
    @Test
    void givenInvalidFlavorValue1NoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setSmokey(-1);
        noteCreateRequest.setPeaty(100);
        noteCreateRequest.setHerbal(-1);
        noteCreateRequest.setBriny(100);
        noteCreateRequest.setVanilla(-1);
        noteCreateRequest.setFruity(100);
        noteCreateRequest.setFloral(-1);
        noteCreateRequest.setWoody(100);
        noteCreateRequest.setRich(-1);
        noteCreateRequest.setSpicy(100);
        noteCreateRequest.setSweet(-1);
        noteCreateRequest.setSalty(100);
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteCreateRequest, images)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }
    @DisplayName("[NOTE][POST] 노트 생성시 향기 값들이 범위 초과면 예외 (2)")
    @Test
    void givenInvalidFlavorValue2NoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setSmokey(1);
        noteCreateRequest.setPeaty(101);
        noteCreateRequest.setHerbal(1);
        noteCreateRequest.setBriny(101);
        noteCreateRequest.setVanilla(1);
        noteCreateRequest.setFruity(101);
        noteCreateRequest.setFloral(1);
        noteCreateRequest.setWoody(101);
        noteCreateRequest.setRich(1);
        noteCreateRequest.setSpicy(101);
        noteCreateRequest.setSweet(1);
        noteCreateRequest.setSalty(101);
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/note");
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteCreateRequest, images)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTE][POST] 노트 생성시 위스키 색상 Enum이 유효하지 않으면 예외")
    @Test
    void givenInvalidColorEnumNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{
        //Given

        //When & Then
        mvc.perform(
                        multipart("/api/note")
                                .header(JwtProperties.KEY_NAME, token)
                                .param("notebookId", "1l")
                                .param("whiskeyName", "Balvenie")
                                .param("description", "very good")
                                .param("whiskeyColor", "wrong color")
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTE][GET] 노트 정상 조회")
    @Test
    void givenNormalRequest_whenGetNote_thenReturnNoteDetailResponse() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        Note savedNote = noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        List<String> imageUrls = createImageUrls(2);
        List<NoteImage> noteImages = List.of(
                NoteImage.of(savedNote, imageUrls.get(0)),
                NoteImage.of(savedNote, imageUrls.get(1))
        );
        noteImageRepository.saveAll(noteImages);

        //When & Then
        mvc.perform(
                get("/api/note/{noteId}", savedNote.getId())
                        .header(JwtProperties.KEY_NAME, token)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        mapper.writeValueAsString(
                                NoteDetailResponse.fromEntity(savedNote, imageUrls))
                ));
    }

    @DisplayName("[NOTE][GET] 노트목록 정상 조회")
    @Test
    void givenNormalRequest_whenGetNotes_thenReturnNoteDetailResponseList() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        noteCreateRequest.setDescription("note1");
        Note savedNote1 = noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        noteCreateRequest.setDescription("note2");
        Note savedNote2 = noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook));

        List<String> imageUrls = createImageUrls(4);
        List<NoteImage> noteImages1 = List.of(
                NoteImage.of(savedNote1, imageUrls.get(0)),
                NoteImage.of(savedNote1, imageUrls.get(1))
        );
        List<NoteImage> noteImages2 = List.of(
                NoteImage.of(savedNote2, imageUrls.get(2)),
                NoteImage.of(savedNote2, imageUrls.get(3))
        );

        noteImageRepository.saveAll(noteImages1);
        noteImageRepository.saveAll(noteImages2);

        //When & Then
        mvc.perform(
                        get("/api/note")
                                .header(JwtProperties.KEY_NAME, token)
                                .queryParam("notebookId", "1")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        mapper.writeValueAsString(
                                List.of(
                                        NoteDetailResponse.fromEntity(
                                                savedNote1,
                                                noteImages1.stream().map(NoteImage::getUrl).collect(Collectors.toList()))
                                        ,
                                        NoteDetailResponse.fromEntity(
                                                savedNote2,
                                                noteImages2.stream().map(NoteImage::getUrl).collect(Collectors.toList())
                                        )
                                )
                )));
    }

    @DisplayName("[NOTE][GET] 노트목록 조회 - 쿼리 파라미터가 없는 경우 예외반환")
    @Test
    void givenRequestWithNoQueryParam_whenGetNotes_thenReturn400() throws Exception{
        //Given

        //When & Then
        mvc.perform(
                        get("/api/note")
                                .header(JwtProperties.KEY_NAME, token)
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.MISSING_QUERY_PARAM.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.MISSING_QUERY_PARAM.getMessage()));
    }

    @DisplayName("[NOTE][PUT] 노트 수정 - 노트 정상 수정")
    @Test
    void givenNormalNoteCreateRequest_whenUpdateNote_thenReturnNoteDetailResponse() throws Exception{
        //Given
        List<String> imageUrls = createImageUrls(4);
        //저장되어있던 노트
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        Note savedNote = noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        List<String> savedImageUrls = List.of(
                imageUrls.get(0),
                imageUrls.get(1)
        );
        List<NoteImage> savedNoteImages = List.of(
                NoteImage.of(savedNote, savedImageUrls.get(0)),
                NoteImage.of(savedNote, savedImageUrls.get(1))
        );
        noteImageRepository.saveAll(savedNoteImages);

        //수정할 내용 생성
        NoteCreateRequest noteUpdateRequest = createNormalNoteCreateRequest();
        noteUpdateRequest.setDescription("updated note");
        List<MockMultipartFile> imagesToUpload = createMockMultipartFiles(2);
        Note targetNote = noteUpdateRequest.toEntity(user, whiskey, noteBook);
        targetNote.setId(1l);
        List<String> updatedImageUrls = List.of(
                imageUrls.get(2),
                imageUrls.get(3)
        );
        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(updatedImageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest =
                multipart("/api/note/{noteId}", savedNote.getId());
        multipartRequest.with(request -> {
            request.setMethod("PUT");
            return request;
        });
        mvc.perform(
                        createMultiPartRequest(multipartRequest, noteUpdateRequest, imagesToUpload)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        mapper.writeValueAsString(
                                NoteDetailResponse.fromEntity(targetNote, updatedImageUrls))
                        )
                );

        assertThat(noteRepository.findNoteById(targetNote.getId()).orElseThrow())
                .isEqualTo(targetNote);

        assertThat(noteImageRepository
                .findNoteImageByNote_Id(targetNote.getId())
                .stream().map(NoteImage::getUrl)
                .collect(Collectors.toList())
        ).containsAll(updatedImageUrls)
        .doesNotContain(savedImageUrls.get(0))
        .doesNotContain(savedImageUrls.get(1));

    }

    @DisplayName("[NOTE][DELETE] 노트 삭제 - 노트 정상삭제")
    @Test
    void givenNoteId_whenDeleteNote_thenReturnOk() throws Exception{
        //Given
        NoteCreateRequest noteCreateRequest = createNormalNoteCreateRequest();
        Note savedNote = noteRepository.save(noteCreateRequest.toEntity(user, whiskey, noteBook));
        List<String> savedImageUrls = createImageUrls(2);
        List<NoteImage> savedNoteImage = List.of(
                NoteImage.of(savedNote, savedImageUrls.get(0)),
                NoteImage.of(savedNote, savedImageUrls.get(1))
        );
        noteImageRepository.saveAll(savedNoteImage);

        //When & Then
        mvc.perform(
                delete("/api/note/{noteId}", savedNote.getId())
                        .header(JwtProperties.KEY_NAME, token)
        ).andExpect(status().isOk());

        assertThat(noteRepository.findNoteById(savedNote.getId()))
                .isEmpty();
        assertThat(noteImageRepository.findNoteImageByNote_Id(savedNote.getId()))
                .hasSize(0);
    }

    private NoteCreateRequest createNormalNoteCreateRequest(){
        return NoteCreateRequest.builder()
                .notebookId(noteBook.getId())
                .whiskeyId(whiskey.getId())
                .whiskeyName(whiskey.getName())
                .distiller(whiskey.getDistillery())
                .price(whiskey.getRetailPrice())
                .rating(3.7f)
                .age(12)
                .nose("good")
                .taste("good")
                .finish("good")
                .description("very good")
                .whiskeyColor(WhiskeyColor.AMBER)
                .smokey(0)
                .peaty(100)
                .herbal(0)
                .briny(100)
                .vanilla(0)
                .fruity(100)
                .floral(0)
                .woody(100)
                .rich(0)
                .spicy(100)
                .sweet(0)
                .salty(100)
                .build();
    }

    private List<MockMultipartFile> createMockMultipartFiles(Integer numberOfFile){
        List<MockMultipartFile> multipartFiles = new ArrayList<>();
        final String fileName = "testImage1";
        final String contentType = "png";
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType;
        for(int i = 0; i < numberOfFile; i++){
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
                    "images",
                    fileName + "." + contentType,
                    contentType,
                    fileInputStream);
        }catch (IOException io){
        }
        return null;
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

    private User createUser(String username){
        return User.of(username ,"password1",username + "@email.com","ROLE_USER", null);
    }
}
