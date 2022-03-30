package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.SignInRequest;
import com.jhkim.whiskeynote.api.dto.SignInResponse;
import com.jhkim.whiskeynote.api.dto.SignUpRequest;
import com.jhkim.whiskeynote.core.dto.UserCreateRequest;
import com.jhkim.whiskeynote.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public void signUp(
            SignUpRequest signUpRequest
    ){
        userService.create(
                UserCreateRequest.builder()
                        .email(signUpRequest.getEmail())
                        .password(signUpRequest.getPassword())
                        .username(signUpRequest.getUsername())
                        .authority("ROLE_USER")
                .build()
        );
    }
}
