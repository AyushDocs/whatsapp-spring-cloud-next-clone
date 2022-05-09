package com.whatsapp.messages.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import com.whatsapp.library.Response;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseStatus(BAD_REQUEST)
      public Response<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  errors.put(fieldName, errorMessage);
            });
            return new Response<>(errors, true);
      }
}
