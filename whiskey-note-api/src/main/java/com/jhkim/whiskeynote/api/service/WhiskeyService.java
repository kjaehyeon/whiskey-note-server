package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.entity.Whiskey;
import com.jhkim.whiskeynote.core.entity.WhiskeyImage;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.WhiskeyImageRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhiskeyService {
    private final WhiskeyRepository whiskeyRepository;
    private final WhiskeyImageRepository whiskeyImageRepository;
    private final AwsS3Service awsS3Service;
    @Transactional
    public WhiskeyDetailResponse createWhiskey(
            WhiskeyCreateRequest whiskeyCreateRequest
    ){
        final Whiskey savedWhiskey = whiskeyRepository.save(whiskeyCreateRequest.toEntity());
        List<String> imageUrls = List.of();
        if(!whiskeyCreateRequest.getImages().isEmpty()){
            imageUrls = uploadWhiskeyImages(whiskeyCreateRequest.getImages());
            saveWhiskeyImageUrls(savedWhiskey, imageUrls);
        }
        return WhiskeyDetailResponse.fromEntityAndImageUrls(savedWhiskey, imageUrls);
    }

    private List<String> uploadWhiskeyImages(
            List<MultipartFile> whiskeyImages
    ){
        return awsS3Service.uploadImages(
                whiskeyImages,
                S3Path.WHISKEY_IMAGE.getFolderName()
        );
    }

    private void saveWhiskeyImageUrls(
            Whiskey whiskey,
            List<String> ImageUrls
    ){
        List<WhiskeyImage> whiskeyImages = new ArrayList<>();
        for(String imageUrl : ImageUrls){
            whiskeyImages.add(WhiskeyImage.of(whiskey, imageUrl));
        }
        whiskeyImageRepository.saveAll(whiskeyImages);
    }

    @Transactional
    public WhiskeyDetailResponse getWhiskey(
            Long whiskeyId
    ){
        Whiskey whiskey = getWhiskeyFromWhiskeyId(whiskeyId);
        List<String> imageUrls = getImageUrlsFromWhiskey(whiskey);

        return WhiskeyDetailResponse.fromEntityAndImageUrls(whiskey, imageUrls);
    }

    private Whiskey getWhiskeyFromWhiskeyId(
            Long whiskeyId
    ){
        return whiskeyRepository.findWhiskeyById(whiskeyId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private List<String> getImageUrlsFromWhiskey(
            Whiskey whiskey
    ){
        return whiskeyImageRepository.findWhiskeyImageByWhiskey(whiskey)
                .stream()
                .map(WhiskeyImage::getUrl)
                .collect(Collectors.toList());
    }

    @Transactional
    public WhiskeyDetailResponse updateWhiskey(
            Long whiskeyId,
            WhiskeyCreateRequest whiskeyUpdateRequest
    ) {
        return null;
    }

    @Transactional
    public void deleteWhiskey(
            Long whiskeyId
    ) {

    }
}
