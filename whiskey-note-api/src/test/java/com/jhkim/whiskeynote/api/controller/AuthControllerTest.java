package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.SignInRequest;
import com.jhkim.whiskeynote.api.dto.SignUpRequest;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API컨트롤러 - AUTH")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    public AuthControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ){
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[AUTH][POST] 회원가입 - 정상입력하면 회원정보 추가하고 HTTP 200 리턴")
    @Test
    void givenValidUserInfo_whenSignUp_thenCreateUserAndReturns() throws Exception{
        //Given
        SignUpRequest signUpRequest = SignUpRequest.of(
                "user1",
                "password1",
                "user1@email.com"
        );
        //When & Then
        mvc.perform(
                post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpRequest))
        ).andExpect(status().isOk());
    }

    @DisplayName("[AUTH][POST] 회원가입 - 유효하지 않은 정보 받으면, HTTP 400 리턴")
    @Test
    void givenInvalidUserInfo_whenSignUp_thenReturnConflict() throws Exception{
        //Given
        SignUpRequest signUpRequest = SignUpRequest.of(
                "",
                "p",
                "user1@email.com"
        );
        //When & Then
        mvc.perform(
                        post("/api/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(signUpRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[AUTH][POST] 로그인 - 정상 로그인시 accessToken 발급")
    @Test
    void givenNormalSignInRequest_whenSignIn_thenReturnAccessToken() throws Exception{
        SignInRequest signInRequest = SignInRequest.of(
                "user1@email.com",
                "password1"
        );

        mvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(mapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

}