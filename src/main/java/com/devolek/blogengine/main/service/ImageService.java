package com.devolek.blogengine.main.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile file) throws IOException;

    void resizeImage(File file, String dstFolder, String fileFormat) throws IOException;

    String getRandomPath();
}
