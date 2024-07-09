package com.example.WorldBankingApplication.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.WorldBankingApplication.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor


//This class implements the fileuploadservice and passes the cloudinary class
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {


        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), ObjectUtils.emptyMap())
                .get("secure_url")
                .toString();
    }


}
