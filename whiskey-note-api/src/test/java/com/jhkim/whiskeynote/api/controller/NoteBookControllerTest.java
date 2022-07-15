package com.jhkim.whiskeynote.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookCreateRequest;
import com.jhkim.whiskeynote.api.dto.notebook.NoteBookUpdateResponse;
import com.jhkim.whiskeynote.api.jwt.JwtProperties;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.NoteImage;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[통합테스트] CONTROLLER - NOTEBOOK")
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class NoteBookControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private DatabaseCleanup databaseCleanup;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private NoteBookRepository noteBookRepository;
    @Autowired private NoteRepository noteRepository;
    @Autowired private NoteImageRepository noteImageRepository;

    private User user;
    private String token;

    @BeforeEach
    void set_Up(){
        databaseCleanup.execute();
        user = createUser("user1");
        token = jwtUtils.createToken(user);
        userRepository.save(user);
    }

    private User createUser(String username){
        return User.of(username ,"password1",username + "@email.com","ROLE_USER", null);
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 노트북 정상 생성")
    @Test
    void givenNormalNoteBook_whenCreateNoteBook_thenReturnOk() throws Exception{
        //Given
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("첫번째 노트북", 255,1,255);

        //When & Then
        mvc.perform(
                post("/api/notebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
                        .content(mapper.writeValueAsString(noteBookCreateRequest))
        ).andExpect(status().isOk());

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .contains(noteBookCreateRequest.toEntity(user));
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 요청에 제목이 빈문자열이면 저장하지 않고 HTTP 400")
    @Test
    void givenEmptyTitleNoteBook_whenCreateNotebook_thenReturn200() throws Exception{
        //Given
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

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .doesNotContain(noteBookCreateRequest.toEntity(user));
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 색상값이 null이면 저장하지 않고 validation error")
    @Test
    void givenNullColor_whenCreateNotebook_thenReturn400() throws Exception{
        //Given
        NoteBookCreateRequest noteBookCreateRequest =
                NoteBookCreateRequest.of("notebook", null,1,null);

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

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .doesNotContain(noteBookCreateRequest.toEntity(user));
    }

    @DisplayName("[NOTEBOOK][POST] 노트북 생성 - 색상값이 범위 미만 or 초과이면 저장하지 않고 validation error")
    @Test
    void givenOverRangeColorValue_whenCreateNotebook_thenReturn400() throws Exception{
        //Given
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

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .doesNotContain(noteBookCreateRequest.toEntity(user));
    }

    @DisplayName("[NOTEBOOK][GET] 노트북 조회 - 노트북 정상 조회")
    @Test
    void givenUser_whenGetNoteBooks_thenReturnNotebookList() throws Exception{
        //Given
        List<NoteBook> noteBooks = List.of(
                createNoteBook("notebook1", user),
                createNoteBook("notebook2", user)
        );
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(1), user);
        createNote(noteBooks.get(1), user);

        List<NoteBookDetailResponse> answer =
                List.of(
                        NoteBookDetailResponse.fromEntity(noteBooks.get(0), 3),
                        NoteBookDetailResponse.fromEntity(noteBooks.get(1), 2)
                );

        //When & Then
        mvc.perform(
                get("/api/notebook")
                        .header(JwtProperties.KEY_NAME, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(answer)));
    }

    private NoteBook createNoteBook(String title, User user){
        return noteBookRepository.save(NoteBook.of(title, user, 1,1,1));
    }
    private Note createNote(NoteBook noteBook, User user){
        return noteRepository.save(
                Note.builder()
                        .notebook(noteBook)
                        .writer(user)
                        .whiskeyName("whiskey")
                        .rating(5.0f)
                        .description("description")
                        .build()
        );
    }

    @DisplayName("[NOTEBOOK][GET] 노트북 조회 - 노트북을 가지고 있지 않은 유저일 경우 빈 리스트 반환")
    @Test
    void givenUserWhoHasNothing_whenGetNotebook_thenReturnEmptyList() throws Exception{
        //Given

        //When & Then
        mvc.perform(
                get("/api/notebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @DisplayName("[NOTEBOOK][PUT] 노트북 수정 - 노트북 정상 수정 후 수정 내용 응답")
    @Test
    void givenNormal_whenUpdateNotebook_thenReturnNoteUpdateResponse() throws Exception{
        //Given
        NoteBook originalNoteBook = createNoteBook("original Notebook", user);// rgb :1 1 1
        NoteBookCreateRequest notebookUpdateRequest =
                NoteBookCreateRequest.of("UpdatedNotebook", 255,1,255);
        NoteBookUpdateResponse answer =
                NoteBookUpdateResponse.of(
                        notebookUpdateRequest.getTitle(),
                        notebookUpdateRequest.getRed(),
                        notebookUpdateRequest.getGreen(),
                        notebookUpdateRequest.getBlue()
                );

        //When & Then
        mvc.perform(
                put("/api/notebook/{notebookId}", originalNoteBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
                        .content(mapper.writeValueAsString(notebookUpdateRequest))
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(answer)));

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .contains(notebookUpdateRequest.toEntity(user));
    }

    @DisplayName("[NOTEBOOK][DELETE] 노트북 삭제 - 정상삭제")
    @Test
    void givenNormal_whenDeleteNotebook_thenReturnOk() throws Exception{
        //Given
        NoteBook noteBookToDelete = createNoteBook("notebook to delete", user);
        Note savedNote = createNote(noteBookToDelete, user);
        List<NoteImage> noteImages = List.of(
                NoteImage.of(savedNote, S3Path.NOTE_IMAGE.getFolderName() + "/imageUrl1"),
                NoteImage.of(savedNote, S3Path.NOTE_IMAGE.getFolderName() + "/imageUrl2")
        );
        noteImageRepository.saveAll(noteImages);

        //When & Then
        mvc.perform(
                delete("/api/notebook/{notebookId}", noteBookToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JwtProperties.KEY_NAME, token)
        ).andExpect(status().isOk());

        assertThat(noteBookRepository.findNoteBookByWriter_Username(user.getUsername()))
                .doesNotContain(noteBookToDelete);
        assertThat(noteRepository.findNoteById(savedNote.getId()))
                .isEmpty();
        assertThat(noteImageRepository.findNoteImageByNote_Id(savedNote.getId()))
                .isEmpty();
    }

}