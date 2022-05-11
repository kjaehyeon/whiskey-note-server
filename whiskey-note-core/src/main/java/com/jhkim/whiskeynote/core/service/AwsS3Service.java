package com.jhkim.whiskeynote.core.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-s3.properties")
public class AwsS3Service {

    @Value("cloud.aws.credentials.access-key")
    private final String bucket;
    private final AmazonS3Client amazonS3Client;
    private final String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".bmp"};

    public List<String> uploadFileList(
            List<MultipartFile> multipartFiles,
            String folderName
    ){
        List<String> urlList = new ArrayList<>();
        multipartFiles.forEach(file -> urlList.add(uploadFile(file, folderName)));
        return urlList;
    }

    public String uploadFile(
            MultipartFile file,
            String folderName
    ){
        final String path = bucket+"/"+folderName;
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try{
            InputStream inputStream = file.getInputStream();
            //PutObjectRequest를 파라미터로 넘기면 로컬에 파일 저장하지 않고 업로드 가능
            amazonS3Client.putObject(
                    new PutObjectRequest(
                            path,
                            fileName,
                            inputStream,
                            objectMetadata
                    ).withCannedAcl(CannedAccessControlList.PublicRead) // public read 권한
            );
        }catch (IOException io){
            throw new GeneralException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(path, fileName).toString();
    }

    public void deleteFile(
            String url
    ){
        String[] splittedUrl = url.split("/");
        amazonS3Client.deleteObject(
                new DeleteObjectRequest(
                    bucket+"/"+splittedUrl[splittedUrl.length-2],
                        splittedUrl[splittedUrl.length-1]
                )
        );
    }
    private String createFileName(
            String fileName
    ){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(
            String fileName
    ) {
        //only png, jpg, jpeg, bmp
        try {
            final String extension = fileName.substring(fileName.lastIndexOf("."));
            for(String allowedExtension : allowedExtensions){
                if(allowedExtension.equals(extension)) return extension;
            }
            throw new GeneralException(ErrorCode.INVALID_FILE_FORMAT);
        } catch (StringIndexOutOfBoundsException e) {
            throw new GeneralException(ErrorCode.INVALID_FILE_FORMAT);
        }
    }
}
