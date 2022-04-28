package com.whatsapp.messages.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.ConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {
      @ExceptionHandler(ConstraintViolationException.class)
      @ResponseStatus(BAD_REQUEST)
      public String constraintViolationException(ConstraintViolationException ex) {
            return ex.toString();
      }

}
