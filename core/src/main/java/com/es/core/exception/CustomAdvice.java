package com.es.core.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ControllerAdvice
public class CustomAdvice {

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + ex.getErrorMessage();
    }

    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public String NotFoundPage() {
        return "404";
    }
}
