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
import com.jhkim.whiskeynote.core.repository.querydsl.NoteImageRepositoryCustom;
import com.jhkim.whiskeynote.core.repository.querydsl.NoteRepositoryCustom;
import com.jhkim.whiskeynote.core.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteRepositoryCustom noteRepositoryCustom;
    private final NoteBookRepository noteBookRepository;
    private final NoteImageRepository noteImageRepository;
    private final NoteImageRepositoryCustom noteImageRepositoryCustom;
    private final WhiskeyRepository whiskeyRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public NoteDetailResponse createNote(
            NoteCreateRequest noteCreateRequest,
            User user
    ) {

        final Note savedNote = noteRepository.save(makeNoteFromNoteCreateRequest(user, noteCreateRequest));
        final List<String> imageUrls = uploadAndSaveNoteImages(savedNote, noteCreateRequest);

        return NoteDetailResponse.fromEntity(savedNote, imageUrls);
    }

    private Note makeNoteFromNoteCreateRequest(
            User writer,
            NoteCreateRequest noteCreateRequest
    ){
        final Whiskey whiskey = whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final NoteBook noteBook = noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        return noteCreateRequest.toEntity(writer, whiskey, noteBook);
    }

    private List<String> uploadAndSaveNoteImages(
            Note note,
            NoteCreateRequest noteCreateRequest
    ){
        List<String> urls = new ArrayList<>();
        if(noteCreateRequest.getImages() != null){
            awsS3Service.uploadImages(noteCreateRequest.getImages(), S3Path.NOTE_IMAGE.getFolderName())
                    .forEach(url -> {
                        noteImageRepository.save(NoteImage.of(note, url));
                        urls.add(url);
                    });
        }
        return urls;
    }

    @Transactional
    public void deleteNote(
            Long noteId,
            User user
    ) {
        checkNoteWriterByNoteId(noteId, user);
        deleteNoteImages(noteId);
        noteRepository.deleteNoteById(noteId);
    }

    private void checkNoteWriterByNoteId(Long noteId, User user){
        final Note note = noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        checkNoteWriter(note, user);
    }

    private void checkNoteWriter(Note note, User user){
        if(!note.getWriter().getUsername().equals(user.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
    }
    private void deleteNoteImages(Long noteId){
        final List<NoteImage> noteImages = noteImageRepository.findNoteImageByNote_Id(noteId);
        if(!noteImages.isEmpty()){
            noteImages.forEach(noteImage -> awsS3Service.deleteImage(noteImage.getUrl()));
            noteImageRepositoryCustom.deleteAllByNote_id(noteId);
        }
    }

    @Transactional
    public NoteDetailResponse updateNote(
            Long noteId,
            NoteCreateRequest noteCreateRequest,
            User user
    ) {
        checkNoteWriterByNoteId(noteId, user);

        final Note originalNote = noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final Note updatedNote = updateOriginalNote(originalNote, noteCreateRequest);

        deleteNoteImages(noteId);

        final List<String> imageUrls = uploadAndSaveNoteImages(updatedNote, noteCreateRequest);

        return NoteDetailResponse.fromEntity(updatedNote, imageUrls);
    }

    private Note updateOriginalNote(
            Note originalNote,
            NoteCreateRequest noteCreateRequest
    ){
        final Whiskey whiskey = whiskeyRepository.findWhiskeyById(noteCreateRequest.getWhiskeyId())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        final NoteBook noteBook = noteBookRepository.findNoteBookById(noteCreateRequest.getNotebookId())
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        return noteCreateRequest.updateNoteEntity(originalNote, whiskey, noteBook);
    }

    @Transactional
    public NoteDetailResponse getNote(
            Long noteId
    ) {
        final Note note = noteRepositoryCustom.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        final List<String> imageUrls =
                noteImageRepository.findNoteImageByNote_Id(noteId)
                        .stream().map(NoteImage::getUrl)
                        .collect(Collectors.toList());
        return NoteDetailResponse.fromEntity(note, imageUrls);
    }

    @Transactional
    public List<NoteDetailResponse> getNotes(
            Long notebookId
    ){
        NoteBook noteBook = noteBookRepository.findNoteBookById(notebookId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        return noteRepository.findAllByNotebook(noteBook)
                .stream()
                .map(note -> NoteDetailResponse.fromEntity(
                        note,
                        noteImageRepository.findNoteImageByNote_Id(note.getId())
                                .stream()
                                .map(NoteImage::getUrl)
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
    }
}
