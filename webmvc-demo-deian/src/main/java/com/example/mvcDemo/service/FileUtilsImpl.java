package com.example.mvcDemo.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;

@Service
public class FileUtilsImpl implements FileUtils {

    @Override
    public boolean handleImageFile(MultipartFile file, boolean wasBusted, Model model) {
        if(!file.isEmpty() && file.getOriginalFilename().length() > 0) {
            String fileName = file.getOriginalFilename();
            Matcher matcher = IMAGE_PATTERN.matcher(fileName);
            if(matcher.matches() && !wasBusted) {
                try {
                    handleMultipartFileSave(file);
                } catch (IOException e) {
                    wasBusted = true;
                    model.addAttribute("fileError", "File too big or unreadable");
                }
            } else {
                wasBusted = true;
                model.addAttribute("fileError", "PNG and JPG files only");
            }
        } else {
            wasBusted = true;
            model.addAttribute("fileError", "Image file is required");
        }
        return wasBusted;
    }

    public void handleMultipartFileSave(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (size > 200000) {
            throw new IOException();
        }
        File currentDir = new File(UPLOADS_DIR);
        if(!currentDir.exists()) {
            currentDir.mkdirs();
        }
        String path = currentDir.getAbsolutePath() + "/" + file.getOriginalFilename();
        path = new File(path).getAbsolutePath();
        File f = new File(path);
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(f));
    }
}
