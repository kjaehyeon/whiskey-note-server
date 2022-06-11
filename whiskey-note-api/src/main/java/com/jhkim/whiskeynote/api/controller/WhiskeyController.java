package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.api.service.WhiskeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/whiskey")
public class WhiskeyController {
    private final WhiskeyService whiskeyService;

    @PostMapping
    public ResponseEntity<Void> createWhiskey(
            @Valid @RequestBody WhiskeyCreateRequest whiskeyCreateRequest
    ){
        whiskeyService.createWhiskey(whiskeyCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{whiskeyId}")
    public ResponseEntity<WhiskeyDetailResponse> getWhiskey(
            @PathVariable Long whiskeyId
    ){
        return new ResponseEntity<>(
                whiskeyService.getWhiskey(whiskeyId),
                HttpStatus.OK
        );
    }
}
