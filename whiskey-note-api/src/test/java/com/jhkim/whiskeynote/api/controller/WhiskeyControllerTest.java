package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.api.jwt.JwtProperties;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.repository.*;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import com.jhkim.whiskeynote.core.service.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합테스트] CONTROLLER")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class WhiskeyControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private UserRepository userRepository;
    @Autowired private DatabaseCleanup databaseCleanup;
    @Autowired private WhiskeyRepository whiskeyRepository;
    @Autowired private WhiskeyImageRepository whiskeyImageRepository;
    @Autowired private JwtUtils jwtUtils;

    //aws s3 upload 부분은 mocking
    @MockBean
    private AwsS3Service awsS3Service;

    private String token;

    @BeforeEach
    void setUp(){
        databaseCleanup.execute();
        User user = createUser("admin1");
        token = jwtUtils.createToken(user);
        userRepository.save(user);
    }

    @DisplayName("[WHISKEY][POST] 위스키 정상 생성")
    @Test
    void givenNormalWhiskeyCreateRequest_whenCreateWhiskey_thenReturnOK() throws Exception{
        //Given
        List<MockMultipartFile> images = createMockMultipartFiles(2);
        List<String> imageUrls = createImageUrls(images.size());
        WhiskeyCreateRequest whiskeyCreateRequest =
                WhiskeyCreateRequest.of("glenfiddich", "glenfiddich 15yrs", "glenfiddich",
                        WhiskeyCategory.SINGLE_MALT, WhiskeyDistrict.SPEYSIDE, null, 15, LocalDate.now(),
                        "oloroso sherry cask", 40.0f, 100000, 700, "regular",
                        Bool3.YES, Bool3.YES, Bool3.UNKNOWN, Bool3.UNKNOWN, Bool3.NO , null);
        Whiskey whiskeyToSave = whiskeyCreateRequest.toEntity();
        whiskeyToSave.setId(1L);

        given(awsS3Service.uploadImages(any(), any()))
                .willReturn(imageUrls);

        //When & Then
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/api/whiskey");
        mvc.perform(
                createMockMultipartRequest(multipartRequest, whiskeyCreateRequest , images)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                                mapper.writeValueAsString(
                                        WhiskeyDetailResponse.fromEntityAndImageUrls(whiskeyToSave, imageUrls)
                                )
                        ));

        assertThat(whiskeyRepository.findAll())
                .contains(whiskeyToSave);
        assertThat(whiskeyImageRepository.findAll().stream().map(WhiskeyImage::getUrl))
                .containsAll(imageUrls);
    }

    private MockHttpServletRequestBuilder createMockMultipartRequest(
            MockMultipartHttpServletRequestBuilder multipartRequest,
            WhiskeyCreateRequest whiskeyCreateRequest,
            List<MockMultipartFile> images
    ){
        for(MockMultipartFile image : images){
            multipartRequest.file(image);
        }

        final PropertyDescriptor[] getterDescriptors = ReflectUtils.getBeanGetters(WhiskeyCreateRequest.class);
        for(PropertyDescriptor pd : getterDescriptors){
            try{
                String value = String.valueOf(pd.getReadMethod().invoke(whiskeyCreateRequest));
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

//    @DisplayName("[WHISKEY][POST] 비정상적인 whiskeyCreateRequest인 경우")
//    @ParameterizedTest
//    @MethodSource //여기에 메서드 이름 명시하지 않으면, 테스트 메서드와 이름이 똑같은 메서드를 찾아서 넣는다.
//    void givenInvalidWhiskeyCreateRequest_whenCreateWhiskey_thenReturn400(
//            WhiskeyCreateRequest whiskeyCreateRequest
//    ) {
//        //Given
//
//        //When & Then
//    }

    private static Stream<WhiskeyCreateRequest> givenInvalidWhiskeyCreateRequest_whenCreateWhiskey_thenReturn400(){
        return null;
    }

    private User createUser(String username){
        return User.of(username ,"password1",username + "@email.com","ROLE_ADMIN", null);
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

}