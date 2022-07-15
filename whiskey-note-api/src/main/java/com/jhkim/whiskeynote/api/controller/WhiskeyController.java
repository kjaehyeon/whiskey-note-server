package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyDetailResponse;
import com.jhkim.whiskeynote.api.service.WhiskeyService;
import com.jhkim.whiskeynote.core.dto.UserDto;
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

    //관리자만 가능하도록 하자.
    @PostMapping
    public ResponseEntity<WhiskeyDetailResponse> createWhiskey(
            @Valid @ModelAttribute WhiskeyCreateRequest whiskeyCreateRequest
    ){
        return new ResponseEntity<>(
                whiskeyService.createWhiskey(whiskeyCreateRequest),
                HttpStatus.OK
        );
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

    @PutMapping("/{whiskeyId}")
    public ResponseEntity<WhiskeyDetailResponse> updateWhiskey(
            @PathVariable Long whiskeyId,
            @Valid @RequestBody WhiskeyCreateRequest whiskeyUpdateRequest
    ){
        return new ResponseEntity<>(
                whiskeyService.updateWhiskey(whiskeyId, whiskeyUpdateRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{whiskeyId}")
    public ResponseEntity<Void> deleteWhiskey(
            @PathVariable Long whiskeyId
    ){
        whiskeyService.deleteWhiskey(whiskeyId);
        return ResponseEntity.ok().build();
    }
}
