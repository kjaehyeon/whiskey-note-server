package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.note.NoteCreateRequest;
import com.jhkim.whiskeynote.api.dto.note.NoteDetailResponse;
import com.jhkim.whiskeynote.core.constant.S3Path;
import com.jhkim.whiskeynote.core.dto.UserDto;
import com.jhkim.whiskeynote.core.entity.*;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.*;
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
    private final NoteBookRepository noteBookRepository;
    private final NoteImageRepository noteImageRepository;
    private final UserRepository userRepository;
    private final WhiskeyRepository whiskeyRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public NoteDetailResponse createNote(
            NoteCreateRequest noteCreateRequest,
            UserDto userDto
    ) {
        final User user = getUserFromUserDto(userDto);
        final Note savedNote = noteRepository.save(makeNoteFromNoteCreateRequest(user, noteCreateRequest));
        final List<String> imageUrls = uploadAndSaveNoteImages(savedNote, noteCreateRequest);

        return NoteDetailResponse.fromEntityAndImageUrls(savedNote, imageUrls);
    }

    private User getUserFromUserDto(UserDto userDto){
        return userRepository.findUserByUsername(userDto.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
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
    public NoteDetailResponse getNote(
            Long noteId
    ) {
        final Note note = getNoteFromNoteId(noteId);
        final List<String> imageUrls = getNoteImageUrls(noteId);

        return NoteDetailResponse.fromEntityAndImageUrls(note, imageUrls);
    }

    private Note getNoteFromNoteId(
            Long noteId
    ){
        return noteRepository.findNoteById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private List<String> getNoteImageUrls(Long noteId){
        return noteImageRepository.findNoteImageByNote_Id(noteId)
                .stream().map(NoteImage::getUrl)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<NoteDetailResponse> getNotes(
            Long notebookId
    ){
        NoteBook noteBook = noteBookRepository.findNoteBookById(notebookId)
                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));

        return noteRepository.findAllByNotebook(noteBook)
                .stream()
                .map(note -> NoteDetailResponse.fromEntityAndImageUrls(note, getNoteImageUrls(note.getId()))
                ).collect(Collectors.toList());
    }

    @Transactional
    public NoteDetailResponse updateNote(
            Long noteId,
            NoteCreateRequest noteCreateRequest,
            UserDto userDto
    ) {
        checkNoteWriterByNoteId(noteId, userDto);

        final Note originalNote = getNoteFromNoteId(noteId);
        final Note updatedNote = updateOriginalNote(originalNote, noteCreateRequest);

        deleteNoteImages(noteId);

        final List<String> imageUrls = uploadAndSaveNoteImages(updatedNote, noteCreateRequest);

        return NoteDetailResponse.fromEntityAndImageUrls(updatedNote, imageUrls);
    }

    private void checkNoteWriterByNoteId(
            Long noteId,
            UserDto userDto
    ){
        final Note note = getNoteFromNoteId(noteId);
        checkNoteWriter(note, userDto);
    }

    private void checkNoteWriter(
            Note note,
            UserDto userDto
    ){
        if(!note.getWriter().getUsername().equals(userDto.getUsername())){
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
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

    private void deleteNoteImages(
            Long noteId
    ){
        final List<NoteImage> noteImages = noteImageRepository.findNoteImageByNote_Id(noteId);
        if(!noteImages.isEmpty()){
            noteImages.forEach(noteImage -> awsS3Service.deleteImage(noteImage.getUrl()));
            noteImageRepository.deleteAllByNote_id(noteId);
        }
    }

    @Transactional
    public void deleteNote(
            Long noteId,
            UserDto userDto
    ) {
        checkNoteWriterByNoteId(noteId, userDto);
        deleteNoteImages(noteId);
        noteRepository.deleteNoteById(noteId);
    }
}
