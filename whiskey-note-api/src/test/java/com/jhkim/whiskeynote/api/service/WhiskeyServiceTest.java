package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.constant.Bool3;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.constant.WhiskeyCategory;
import com.jhkim.whiskeynote.core.constant.WhiskeyDistrict;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import com.jhkim.whiskeynote.core.repository.WhiskeyImageRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[유닛테스트] SERVICE - WHISKEY")
@ExtendWith(MockitoExtension.class)
class WhiskeyServiceTest {
    @InjectMocks private WhiskeyService sut;
    @Mock private WhiskeyRepository whiskeyRepository;
    @Mock private AwsS3Service awsS3Service;
    @Mock private WhiskeyImageRepository whiskeyImageRepository;

    /**
     * createWhiskey()
     */
    @DisplayName("[WHISKEY][CREATE] 위스키 정상 생성 요청 + 이미지")
    @Test
    void givenNormalWhiskeyCreateRequest_whenCreateWhiskey_thenReturnWhiskeyDetailResponse(){
        //Given
        WhiskeyCreateRequest whiskeyCreateRequest =
                createWhiskeyCreateRequest(createMultiFiles(2));
        Whiskey whiskey = whiskeyCreateRequest.toEntity();
        whiskey.setId(1L);
        List<String> urls = List.of("url1", "url2");

        given(awsS3Service.uploadImages(whiskeyCreateRequest.getImages(), S3Path.WHISKEY_IMAGE.getFolderName()))
                .willReturn(urls);
        given(whiskeyRepository.save(whiskeyCreateRequest.toEntity()))
                .willReturn(whiskey);

        //When
        WhiskeyDetailResponse result = sut.createWhiskey(whiskeyCreateRequest);

        //Then
        then(whiskeyRepository).should().save(whiskey);
        then(whiskeyImageRepository).should().saveAll(any());
        assertThat(result).isEqualTo(WhiskeyDetailResponse.fromEntityAndImageUrls(whiskey, urls));
    }
    private WhiskeyCreateRequest createWhiskeyCreateRequest(
            List<MultipartFile> multipartFiles
    ){
        return WhiskeyCreateRequest.builder()
                .brand("Macallan")
                .name("sherry cask 12yr")
                .distillery("Macallan")
                .category(WhiskeyCategory.SINGLE_MALT)
                .district(WhiskeyDistrict.SPEYSIDE)
                .bottler("증류소 보틀링")
                .statedAge(12)
                .vintage(LocalDate.now())
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
                .images(multipartFiles)
                .build();
    }
    /**
     * getWhiskey()
     */

    private List<MultipartFile> createMultiFiles(Integer num){
        List<MultipartFile> multipartFiles = new ArrayList<>();
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
                    fileName,
                    fileName + "." + contentType,
                    contentType,
                    fileInputStream);
        }catch (IOException io){
        }
        return null;
    }
}