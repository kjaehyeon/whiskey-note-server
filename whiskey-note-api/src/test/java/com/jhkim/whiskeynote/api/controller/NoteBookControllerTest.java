package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.jwt.JwtProperties;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API컨트롤러 - NOTEBOOK")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class NoteBookControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private DatabaseCleanup databaseCleanup;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;


    @BeforeEach
    void set_Up(){
        databaseCleanup.execute();
        userRepository.save(createUser("user1"));
    }
    private User createUser(String username){
        return User.of(username ,"password1",username + "@email.com","ROLE_USER", null);
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 노트북 정상 생성")
    @Test
    void givenNormalNoteBook_whenCreateNoteBook_thenReturnOk() throws Exception{
        //Given
        String token = jwtUtils.createToken(createUser("user1"));
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("첫번째 노트북", 255,1,255);
        //When & Then
        mvc.perform(
                post("/api/notebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
                        .content(mapper.writeValueAsString(noteBookCreateRequest))
        ).andExpect(status().isOk());
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 요청에 제목이 빈문자열이면 HTTP 400")
    @Test
    void givenEmptyTitleNoteBook_whenCreateNotebook_thenReturn200() throws Exception{
        //Given
        String token = jwtUtils.createToken(createUser("user1"));
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("", 255,1,255);
        //When & Then
        mvc.perform(
                post("/api/notebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
                        .content(mapper.writeValueAsString(noteBookCreateRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 색상값이 범위 null")
    @Test
    void givenNullColor_whenCreateNotebook_thenReturn400() throws Exception{
        //Given
        String token = jwtUtils.createToken(createUser("user1"));
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("", null,1,null);
        //When & Then
        mvc.perform(
                post("/api/notebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
                        .content(mapper.writeValueAsString(noteBookCreateRequest))
        ).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 색상값이 범위 미만 or 초과")
    @Test
    void givenOverRangeColorValue_whenCreateNotebook_thenReturn400() throws Exception{
        //Given
        String token = jwtUtils.createToken(createUser("user1"));
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("", 256,-1,255);
        //When & Then
        mvc.perform(
                        post("/api/notebook")
                                .header(JwtProperties.KEY_NAME, token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(noteBookCreateRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }


}