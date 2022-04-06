package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhiskeyController {

    @GetMapping("/test")
    public ResponseEntity<Void> test(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/user")
    public ResponseEntity<Void> testuser(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/admin")
    public ResponseEntity<Void> testadmin(){
        return ResponseEntity.ok().build();
    }
}
