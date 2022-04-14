package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[유닛테스트] SERVICE - Note 서비스")
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @InjectMocks
    private NoteService sut;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private NoteRepository noteRepository;
}
