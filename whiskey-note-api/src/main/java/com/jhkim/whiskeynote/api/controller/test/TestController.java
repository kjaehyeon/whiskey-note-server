package com.jhkim.whiskeynote.api.controller.test;

import com.jhkim.whiskeynote.core.dto.UserDto;
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
            UserDto userDto
    ){
        return ResponseEntity.ok().build();
    }
}
