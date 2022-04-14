package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.whiskey.WhiskeyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/whiskey")
public class WhiskeyController {

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody WhiskeyCreateRequest whiskeyCreateRequest
    ){
        return ResponseEntity.ok().build();
    }

}
