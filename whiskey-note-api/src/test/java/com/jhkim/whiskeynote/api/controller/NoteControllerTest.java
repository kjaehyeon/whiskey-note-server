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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API컨트롤러 - NOTE")
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

        List<MockMultipartFile> images = createMultiFiles(2);
        List<String> imageUrls = List.of("imageUrl1", "imageUrl2");

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        mvc.perform(
                multipart("/api/note")
                        .file(images.get(0)).file(images.get(1))
                        .param("notebookId", noteCreateRequest.getNotebookId().toString())
                        .param("whiskeyId", noteCreateRequest.getWhiskeyId().toString())
                        .param("whiskeyName", noteCreateRequest.getWhiskeyName())
                        .param("distiller", noteCreateRequest.getDistiller())
                        .param("price", noteCreateRequest.getPrice().toString())
                        .param("rating", noteCreateRequest.getRating().toString())
                        .param("age", noteCreateRequest.getAge().toString())
                        .param("nose", noteCreateRequest.getNose())
                        .param("taste", noteCreateRequest.getTaste())
                        .param("finish", noteCreateRequest.getFinish())
                        .param("description", noteCreateRequest.getDescription())
                        .param("whiskeyColor", noteCreateRequest.getWhiskeyColor().toString())
                        .param("smokey", noteCreateRequest.getSmokey().toString())
                        .param("peaty", noteCreateRequest.getPeaty().toString())
                        .param("herbal", noteCreateRequest.getHerbal().toString())
                        .param("briny", noteCreateRequest.getBriny().toString())
                        .param("vanilla", noteCreateRequest.getVanilla().toString())
                        .param("fruity", noteCreateRequest.getFruity().toString())
                        .param("floral", noteCreateRequest.getFloral().toString())
                        .param("woody", noteCreateRequest.getWoody().toString())
                        .param("rich", noteCreateRequest.getRich().toString())
                        .param("spicy", noteCreateRequest.getSpicy().toString())
                        .param("sweet", noteCreateRequest.getSweet().toString())
                        .param("salty", noteCreateRequest.getSalty().toString())
                        .header(JwtProperties.KEY_NAME, token)
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
                .contains(imageUrls.get(0))
                .contains(imageUrls.get(1));
    }

    @DisplayName("[NOTE][POST] 노트 생성시 노트북ID가 null이면 예외")
    @Test
    void givenNullNotebookIdNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{

    }

    @DisplayName("[NOTE][POST] 노트 생성시 위스키 이름이 빈칸이면 예외")
    @Test
    void givenBlankWhiskeyNameNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{

    }

    @DisplayName("[NOTE][POST] 노트 생성시 평점이 범위 초과면 예외")
    @Test
    void givenInvalidRatingNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{

    }

    @DisplayName("[NOTE][POST] 노트 생성시 향기 값들이 범위 초과면 예외")
    @Test
    void givenInvalidFlavorValueNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{

    }

    @DisplayName("[NOTE][POST] 노트 생성시 위스키 색상 Enum이 유효하지 않으면 예외")
    @Test
    void givenInvalidColorEnumNoteCreateRequest_whenCreateNote_thenReturn400() throws Exception{

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
                .smokey(1)
                .peaty(100)
                .herbal(1)
                .briny(100)
                .vanilla(1)
                .fruity(100)
                .floral(1)
                .woody(100)
                .rich(1)
                .spicy(100)
                .sweet(1)
                .salty(100)
                .build();
    }
    private List<MockMultipartFile> createMultiFiles(Integer num){
        List<MockMultipartFile> multipartFiles = new ArrayList<>();
        final String fileName = "testImage1";
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
