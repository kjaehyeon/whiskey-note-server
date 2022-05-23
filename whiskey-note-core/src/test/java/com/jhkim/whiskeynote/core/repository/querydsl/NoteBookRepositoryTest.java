package com.jhkim.whiskeynote.core.repository.querydsl;

import com.jhkim.whiskeynote.core.config.QuerydslConfiguration;
import com.jhkim.whiskeynote.core.dto.NoteBookDetailResponse;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteBook;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("REPOSITORY - NOTEBOOKCUSTOM")
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DataJpaTest
@Import(QuerydslConfiguration.class)
class NoteBookRepositoryTest {
    @Autowired private NoteRepository noteRepository;
    @Autowired private NoteBookRepository noteBookRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("findNoteBookAndNoteCntByWriterName - 유저 이름으로 조회하면, 노트목록 + 노트 개수")
    @Test
    void givenUserName_whenFindNoteBookAndNoteCntByWriterName_thenReturnResult(){
        //Given
        User user = createUser("user1");
        List<NoteBook> noteBooks = List.of(
                createNoteBook("notebook1", user),
                createNoteBook("notebook2", user)
        );
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(0), user);
        createNote(noteBooks.get(1), user);
        createNote(noteBooks.get(1), user);

        //When
        List<NoteBookDetailResponse> noteBookDetailResponses
                 = noteBookRepository.findNoteBookAndNoteCntByWriterName(user.getUsername());
        //Then
        List<NoteBookDetailResponse> answer =
                List.of(
                        NoteBookDetailResponse.fromEntity(noteBooks.get(0),3),
                        NoteBookDetailResponse.fromEntity(noteBooks.get(1),2)
                );
        assertThat(noteBookDetailResponses)
                .hasSize(2)
                .contains(answer.get(0), Index.atIndex(0))
                .contains(answer.get(1), Index.atIndex(1));
    }

    private User createUser(String username){
        User user = User.of(username, "password1", username+"@email.com", "ROLE_USER",null);
        userRepository.save(user);
        return user;
    }
    private NoteBook createNoteBook(String title, User user){
        return noteBookRepository.save(NoteBook.of(title, user, 1,1,1));
    }
    private void createNote(NoteBook noteBook, User user){
        noteRepository.save(
                Note.builder()
                        .notebook(noteBook)
                        .writer(user)
                        .whiskeyName("whiskey")
                        .rating(5.0f)
                        .description("description")
                        .build()
        );
    }
}