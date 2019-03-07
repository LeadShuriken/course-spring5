package com.example.mvcDemo.service;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

public interface FileUtils {
    String UPLOADS_DIR = "tmp";
    Pattern IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png))$)");

    boolean handleImageFile(MultipartFile file, boolean wasBusted, Model model);
    void handleMultipartFileSave(MultipartFile file) throws IOException;
}
