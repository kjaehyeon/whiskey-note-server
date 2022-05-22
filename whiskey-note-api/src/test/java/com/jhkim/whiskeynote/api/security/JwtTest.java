package com.jhkim.whiskeynote.api.security;

import com.jhkim.whiskeynote.api.jwt.JwtKey;
import com.jhkim.whiskeynote.api.jwt.JwtProperties;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Key;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[SECURITY] JWT TEST")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class JwtTest {
    private final MockMvc mvc;
    private final JwtUtils jwtUtils;
    private final JwtKey jwtKey;

    public JwtTest(
            @Autowired MockMvc mvc,
            @Autowired JwtUtils jwtUtils,
            @Autowired JwtKey jwtKey
    ){
        this.mvc = mvc;
        this.jwtUtils = jwtUtils;
        this.jwtKey = jwtKey;
    }

    @BeforeAll
    static void set_up(
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder
    ){
        userRepository.save(User.builder()
                .username("user1")
                .password(passwordEncoder.encode("password1"))
                .email("user1@email.com")
                .authority("ROLE_USER")
                .build()
        );
    }

    @DisplayName("[JWT] 정상 토큰으로 요청")
    @Test
    void givenNormalJwtToken_whenRequest_thenOk() throws Exception{
        //Given
        User user = User.builder()
                .username("user1")
                .password("password1")
                .build();
        String NormalToken = jwtUtils.createToken(user);
        //When & Then
        mvc.perform(
                get("/test/jwt")
                        .header(JwtProperties.KEY_NAME, NormalToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @DisplayName("[JWT] 만료 시간이 지난 토큰으로 요청")
    @Test
    void givenExpiredJwtToken_whenRequest_thenException() throws Exception{
        //Given
        User user = User.builder()
                .username("user1")
                .password("password1")
                .build();
        String expiredToken = createExpiredToken(user);
        //When & Then
        mvc.perform(
                get("/test/jwt")
                        .header(JwtProperties.KEY_NAME, expiredToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(ErrorCode.TOKEN_EXPIRED.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_EXPIRED.getMessage()));
    }

    @DisplayName("[JWT] 유효하지 않은 토큰으로 요청")
    @Test
    void givenInvalidJwtToken_whenRequest_thenException() throws Exception{
        //Given
        User user = User.builder()
                .username("user1")
                .password("password1")
                .build();
        String invalidToken = jwtUtils.createToken(user) + "Invalid String";
        //When & Then
        mvc.perform(
                        get("/test/jwt")
                                .header(JwtProperties.KEY_NAME, invalidToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN.getMessage()));
    }

    @DisplayName("[JWT] 빈 토큰으로 요청")
    @Test
    void givenEmptyJwtToken_whenRequest_thenException() throws Exception{
        //Given
        String emptyToken = "";
        //When & Then
        mvc.perform(
                        get("/test/jwt")
                                .header(JwtProperties.KEY_NAME, emptyToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN.getMessage()));
    }

    @DisplayName("[JWT] JWT 토큰 헤더 없이 요청")
    @Test
    void givenNoJwtHeader_whenRequest_thenException() throws Exception{
        //Given
        //When & Then
        mvc.perform(
                    get("/test/jwt")
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnauthorized())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHENTICATED.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.UNAUTHENTICATED.getMessage()));
    }

    public String createExpiredToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date now = new Date();
        Pair<String, Key> key = jwtKey.getRandomKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() - 1))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }
}
