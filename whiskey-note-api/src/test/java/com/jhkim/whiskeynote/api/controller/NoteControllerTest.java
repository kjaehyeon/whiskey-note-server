package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import com.jhkim.whiskeynote.core.service.DatabaseCleanup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API컨트롤러 - NOTE")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class NoteControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final UserRepository userRepository;
    private final DatabaseCleanup databaseCleanup;
    private final PasswordEncoder passwordEncoder;

    public NoteControllerTest(
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

    @DisplayName("[NOTE][POST] 노트 정상 생성")
    @Test
    void givenNormalNoteCreateRequest_whenCreateNote_thenOk() throws Exception{

    }
    //whiskey color가 존재하지 않는 Enum인 경우(이것도 Controller validation 테스트)
    //참조하는 노트북 ID 없이 노트생성요청 (이거는 ControllerTest에서)
}
