package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.entity.Note;
import com.jhkim.whiskeynote.core.entity.NoteImage;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteImageRepository noteImageRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void create(
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        //파일 뽑아서 s3에 업로드 하고 url 받아옴
        //받아온 url NoteImage 테이블에 저장
        //Note 엔티티도 만들어서 저장
        final Note savedNote = noteRepository.save(noteCreateRequest.toEntity(noteCreateRequest));
        awsS3Service.uploadFileList(
                noteCreateRequest.getImages(),
                S3Path.NOTE_IMAGE.getFolderName()
        ).forEach(url ->noteImageRepository.save(NoteImage.of(savedNote, url)));
    }

    @Transactional
    public void delete(
            Long noteId,
            User user
    ) {
        noteRepository.deleteNoteById(noteId);
        noteImageRepository.deleteAllByNote_Id(noteId);
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
