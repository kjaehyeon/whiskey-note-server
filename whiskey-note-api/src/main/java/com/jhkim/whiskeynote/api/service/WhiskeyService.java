package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhiskeyService {
    private final WhiskeyRepository whiskeyRepository;

    @Transactional
    public WhiskeyDetailResponse createWhiskey(
            WhiskeyCreateRequest whiskeyCreateRequest
    ){
        whiskeyRepository.save(whiskeyCreateRequest.toEntity());
        return null;
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
