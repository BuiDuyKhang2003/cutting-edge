package com.exe201.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryUploadService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
