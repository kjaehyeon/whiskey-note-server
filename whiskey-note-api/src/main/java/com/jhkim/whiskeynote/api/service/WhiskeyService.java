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

import java.util.ArrayList;
import java.util.List;

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

        final List<String> imageUrls =
                awsS3Service.uploadImages(
                        whiskeyCreateRequest.getImages(),
                        S3Path.WHISKEY_IMAGE.getFolderName()
                );

        List<WhiskeyImage> whiskeyImages = new ArrayList<>();
        for(String imageUrl : imageUrls){
            whiskeyImages.add(WhiskeyImage.of(savedWhiskey, imageUrl));
        }
        whiskeyImageRepository.saveAll(whiskeyImages);
        return WhiskeyDetailResponse.fromEntityAndImageUrls(savedWhiskey, imageUrls);
    }

    @Transactional
    public WhiskeyDetailResponse getWhiskey(
            Long whiskeyId
    ){
        return WhiskeyDetailResponse.fromEntityAndImageUrls(
                whiskeyRepository.findWhiskeyById(whiskeyId)
                        .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND)),
                List.of()
        );
    }

    public WhiskeyDetailResponse updateWhiskey(
            Long whiskeyId,
            WhiskeyCreateRequest whiskeyUpdateRequest
    ) {
        return null;
    }

    public void deleteWhiskey(
            Long whiskeyId
    ) {

    }
}
