package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.NoteBookRepository;
import com.jhkim.whiskeynote.core.repository.NoteImageRepository;
import com.jhkim.whiskeynote.core.repository.NoteRepository;
import com.jhkim.whiskeynote.core.repository.WhiskeyRepository;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteBookRepository noteBookRepository;
    private final NoteImageRepository noteImageRepository;
    private final WhiskeyRepository whiskeyRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void createNote(
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        //파일 뽑아서 s3에 업로드 하고 url 받아옴
        //받아온 url NoteImage 테이블에 저장
        //Note 엔티티도 만들어서 저장

        final Whiskey whiskey = whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskey_id())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final NoteBook noteBook = noteBookRepository.findNoteBookById(noteCreateRequest.getNotebook_id())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        final Note savedNote = noteRepository.save(
                noteCreateRequest.toEntity(
                        noteCreateRequest,
                        whiskey,
                        noteBook
                ));

        awsS3Service.uploadImages(
                noteCreateRequest.getImages(),
                S3Path.NOTE_IMAGE.getFolderName()
        ).forEach(url -> noteImageRepository.save(NoteImage.of(savedNote, url)));
    }

    @Transactional
    public void deleteNote(
            Long noteId,
            User user
    ) {
        checkNoteWriterByNoteId(noteId, user);
        //note 지워지면 s3에 있는 이미지도 다 지우기
        noteImageRepository.findNoteImageByNote_Id(noteId)
                        .forEach(noteImage -> {
                            awsS3Service.deleteImage(noteImage.getUrl());
                        });
        noteRepository.deleteNoteById(noteId);
    }

    @Transactional
    public NoteDetailResponse getNote(
            Long noteId,
            User user
    ) {
        final Note note = noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final List<String> noteImageUrls =
                noteImageRepository.findNoteImageByNote_Id(noteId)
                        .stream().map(NoteImage::getUrl)
                        .collect(Collectors.toList());
        return NoteDetailResponse.fromEntity(note, noteImageUrls);
    }

    //TODO
    @Transactional
    public List<NoteDetailResponse> getNotes(
            Long notebookId,
            User user
    ){
        return noteRepository.findAllByNotebook_Id(notebookId)
                .stream()
                .map(note -> NoteDetailResponse.fromEntity(
                        note,
                        noteImageRepository.findNoteImageByNote_Id(note.getId())
                                .stream()
                                .map(NoteImage::getUrl)
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    @Transactional
    public NoteDetailResponse updateNote(
            Long noteId,
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        final Note note = noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        checkNoteWriter(note, user);

        final Whiskey whiskey = whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskey_id())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final NoteBook noteBook = noteBookRepository.findNoteBookById(noteCreateRequest.getNotebook_id())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));


        final List<NoteImage> noteImages = noteImageRepository.findNoteImageByNote_Id(noteId);

        if(!noteImages.isEmpty()){
            noteImages.forEach(noteImage -> awsS3Service.deleteImage(noteImage.getUrl()));
            noteImageRepository.deleteAllByNote_Id(noteId);
        }

        return NoteDetailResponse.fromEntity(
                noteRepository.save(noteCreateRequest.updateEntity(note,whiskey,noteBook)),
                awsS3Service.uploadImages(
                        noteCreateRequest.getImages(),
                        S3Path.NOTE_IMAGE.getFolderName()
                )
        );
    }

    private void checkNoteWriter(Note note, User user){
        if(!note.getNotebook().getWriter().getUsername().equals(user.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
    }
    private void checkNoteWriterByNoteId(Long noteId, User user){
        final Note note = noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        checkNoteWriter(note, user);
    }
}
