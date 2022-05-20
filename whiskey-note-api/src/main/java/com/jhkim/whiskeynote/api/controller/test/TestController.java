package com.jhkim.whiskeynote.api.controller.test;

import com.jhkim.whiskeynote.core.entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
@RequestMapping("/test")
public class TestController {
    @GetMapping("/jwt")
    public ResponseEntity<Void> jwtTest(
            User user
    ){
        return ResponseEntity.ok().build();
    }
}
