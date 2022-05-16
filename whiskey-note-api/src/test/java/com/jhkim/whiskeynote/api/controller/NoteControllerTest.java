package com.jhkim.whiskeynote.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("API컨트롤러 - NOTE")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class NoteControllerTest {
    //whiskey color가 존재하지 않는 Enum인 경우(이것도 Controller validation 테스트)
    //참조하는 노트북 ID 없이 노트생성요청 (이거는 ControllerTest에서)
}
