package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${user.photo.width}")
    private int userPhotoWidth;

    @Override
    public String saveImage(MultipartFile file, int width) throws IOException {
        String visibleDirPath = "/upload" + getRandomPath();
        String dirPath = uploadPath + visibleDirPath;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);

        String newFileName = UUID.randomUUID().toString() + "." + fileName;

        String filePath = dirPath + newFileName;
        String visibleFilePath = visibleDirPath + newFileName;

        File fullFile = new File(filePath);

        file.transferTo(fullFile);
        resizeImage(fullFile, filePath, fileFormat, width);

        return visibleFilePath;
    }

    @Override
    public void resizeImage(File file, String dstFolder, String fileFormat, int width) throws IOException {
        BufferedImage image = ImageIO.read(file);

        int newHeight = width == userPhotoWidth ? userPhotoWidth : (int) Math.round(
                image.getHeight() / (image.getWidth() / (double) width));

        BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
                width * 4, newHeight * 4, Scalr.OP_ANTIALIAS);
        BufferedImage scaledImg2 = Scalr.resize(scaledImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
                width, newHeight, Scalr.OP_ANTIALIAS);

        File newFile = new File(dstFolder);
        ImageIO.write(scaledImg2, fileFormat, newFile);
    }

    @Override
    public String getRandomPath() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            if (i == 0 || i == 3 || i == 6 || i == 9) {
                sb.append("/");
                continue;
            }
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
