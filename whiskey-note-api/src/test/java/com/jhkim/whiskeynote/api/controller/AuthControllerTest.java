package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.LogInRequest;
import com.jhkim.whiskeynote.api.dto.SignUpRequest;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import com.jhkim.whiskeynote.core.service.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[통합테스트] API컨트롤러 - AUTH")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final UserRepository userRepository;
    private final DatabaseCleanup databaseCleanup;
    private final PasswordEncoder passwordEncoder;

    public AuthControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired UserRepository userRepository,
            @Autowired DatabaseCleanup databaseCleanup,
            @Autowired PasswordEncoder passwordEncoder
    ){
        this.mvc = mvc;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.databaseCleanup = databaseCleanup;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void set_up(){
        databaseCleanup.execute();
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
        //when & then
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
        //when & then
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
        //given
        createNormalUser("user1", "password1");
        LogInRequest logInRequest = LogInRequest.of(
                "user1",
                "password1"
        );

        //when & then
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(logInRequest))
        ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @DisplayName("[AUTH][POST] 회원가입 - 회원정보 중복 될때 USER_ALREADY_EXISTS 예외 반환")
    @Test
    void givenDuplicatedUser_whenSignUp_ThenReturnException() throws Exception{
        //Given
        createNormalUser("user1", "password1");
        SignUpRequest signUpRequest = SignUpRequest.of(
                "user1",
                "password1",
                "user1@email.com"
        );
        //When & Then
        mvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signUpRequest))
        ).andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_ALREADY_EXISTS.getCode()));
    }

    @DisplayName("[API][POST] 로그인 - 아이디가 없는 경우")
    @Test
    void givenWrongUsername_whenSignIn_thenReturnException() throws Exception{
        //given
        createNormalUser("user1", "password1");
        LogInRequest logInRequest = LogInRequest.of(
                "user2",
                "password1"
        );
        //when & then
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(logInRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
    }

    //회원 비밀번호 틀렸을때
    @DisplayName("[API][POST] 로그인 - 비밀번호를 틀린경우")
    @Test
    void givenWrongPassword_whenLogIn_thenReturnException() throws Exception{
        //given
        createNormalUser("user1", "password1");
        LogInRequest logInRequest = LogInRequest.of(
                "user1",
                "wrongpassword"
        );
        //when & then
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(logInRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ErrorCode.PASSWORD_NOT_MATCH.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.PASSWORD_NOT_MATCH.getCode()));
    }

    private void createNormalUser(String username, String password){
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(username + "@email.com")
                .authority("ROLE_USER")
                .build()
        );
    }

}