package com.whatsapp.profile_service.controllers;

import java.util.HashMap;
import java.util.Map;

import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.exceptions.InvalidCredentialsException;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {
      @ExceptionHandler(InvalidCredentialsException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<Object> invalidCredentialsException(InvalidCredentialsException ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "invalid credentials");
            return new Response<>(errors, true);
      }

      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  errors.put(fieldName, errorMessage);
            });
            return new Response<>(errors, true);
      }

      @ExceptionHandler(UserNotFoundException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public Response<Object> userNotFoundException(UserNotFoundException ex) {
            Map<String, String> errors =Map.of("message", "user not found");
            return new Response<>(errors, true);
      }
}
