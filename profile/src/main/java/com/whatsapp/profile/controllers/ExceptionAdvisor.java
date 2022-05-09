package com.whatsapp.profile.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import com.whatsapp.library.Response;
import com.whatsapp.profile.exceptions.InvalidInputException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  errors.put(fieldName, errorMessage);
            });
            return new Response<>(errors, true);
      }

      @ExceptionHandler(ConstraintViolationException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<String> constraintViolationException(ConstraintViolationException ex) {
            return new Response<>(ex.toString(), true);
      }

      @ExceptionHandler(InvalidInputException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<Object> invalidInputException(InvalidInputException ex) {
            Map<String, String> errors = Map.of("message", ex.getMessage());
            return new Response<>(errors, true);
      }
}