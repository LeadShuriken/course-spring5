package com.example.mvcDemo.web;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.FileSystemException;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler({ MaxUploadSizeExceededException.class, FileSystemException.class })
    @Order(1)
    public ModelAndView handle(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("errors");
        modelAndView.getModel().put("message", ex.getMessage());
        return modelAndView;
    }
}
