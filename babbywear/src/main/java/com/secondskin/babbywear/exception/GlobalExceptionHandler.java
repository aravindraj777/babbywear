package com.secondskin.babbywear.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public String handleMaxSizeException(MaxUploadSizeExceededException exception,Model model){
        model.addAttribute("message","please upload smaller size");
       return  "product-not-found";
    }

}
