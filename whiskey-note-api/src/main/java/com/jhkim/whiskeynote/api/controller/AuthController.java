package com.jhkim.whiskeynote.api.controller;

import com.jhkim.whiskeynote.api.dto.auth.LogInRequest;
import com.jhkim.whiskeynote.api.dto.auth.LoginResponse;
import com.jhkim.whiskeynote.api.dto.auth.SignUpRequest;
import com.jhkim.whiskeynote.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LogInRequest logInRequest
    ){
        return new ResponseEntity<>(authService.login(logInRequest), HttpStatus.OK);
    }
}
