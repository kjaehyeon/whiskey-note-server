package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    @Transactional
    public void create(
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        //파일 뽑아서 s3에 업로드 하고 url 받아옴
        //받아온 url NoteImage 테이블에 저장
        //Note 엔티티도 만들어서 저장
    }

    @Transactional
    public void delete(
            Long noteId,
            User user
    ) {

    }

    @Transactional
    public NoteDetailResponse getNote(
            Long noteId,
            User user
    ) {
        return null;
    }

    @Transactional
    public NoteDetailResponse upsert(
            Long noteId,
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        return null;
    }
}
