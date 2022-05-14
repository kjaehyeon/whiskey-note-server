package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WhiskeyService {
    private final WhiskeyRepository whiskeyRepository;

    @Transactional
    public void createWhiskey(
            WhiskeyCreateRequest whiskeyCreateRequest
    ){
        whiskeyRepository.save(whiskeyCreateRequest.toEntity());
    }

    @Transactional
    public WhiskeyDetailResponse getWhiskey(
            Long whiskeyId
    ){
        return WhiskeyDetailResponse.fromEntity(
                whiskeyRepository.findWhiskeyById(whiskeyId)
                        .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND))
        );
    }
}
