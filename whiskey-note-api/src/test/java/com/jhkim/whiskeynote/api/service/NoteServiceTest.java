package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
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
    @Mock private NoteRepository noteRepository;
    @Mock private NoteBookRepository noteBookRepository;
    @Mock private NoteImageRepository noteImageRepository;
    @Mock private WhiskeyRepository whiskeyRepository;
    @Mock private AwsS3Service awsS3Service;

    //createNote
    //deleteNote
    //getNote
    //getNotes
    //updateNote

}
