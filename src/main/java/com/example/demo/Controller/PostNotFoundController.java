package com.example.demo.Controller;

import com.example.demo.Error.PostNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostNotFoundController {

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(PostNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "/error/post-not-found";
    }
}