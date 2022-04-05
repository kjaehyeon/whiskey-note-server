package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.LogInRequest;
import com.jhkim.whiskeynote.api.dto.LoginResponse;
import com.jhkim.whiskeynote.api.dto.SignUpRequest;
import com.jhkim.whiskeynote.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest
    ){
        authService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/sign-up")
    public ResponseEntity<Void> signUpAdmin(
            @Valid @RequestBody SignUpRequest signUpRequest
    ){
        authService.signUpAdmin(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<LoginResponse> login(
            @Valid LogInRequest logInRequest
    ){
        return new ResponseEntity<>(authService.login(logInRequest), HttpStatus.OK);
    }
}
