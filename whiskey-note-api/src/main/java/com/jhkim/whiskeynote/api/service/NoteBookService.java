package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.NoteBookDto;
import com.jhkim.whiskeynote.api.dto.NoteBookResponse;
import com.jhkim.whiskeynote.api.dto.NoteDto;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.constant.WhiskeyColor;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteBookService {
    private final NoteBookRepository noteBookRepository;

    @Transactional
    public void create(
            NoteBookDto noteBookDto
    ){

    }

    @Transactional
    public void upsert(
            Long notebook_id,
            NoteBookDto noteBookDto
    ){

    }

    @Transactional
    public void delete(
            Long notebook_id
    ){

    }

    @Transactional
    public List<NoteBookResponse> getNoteBookList(
            User user
    ){
        return null;
    }

    @Transactional
    public List<NoteBookResponse> getNoteBook(
            Long notebook_id
    ){
        return null;
    }
}
