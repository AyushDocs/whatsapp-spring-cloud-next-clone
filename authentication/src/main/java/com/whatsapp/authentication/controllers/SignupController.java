package com.whatsapp.authentication.controllers;

import javax.validation.Valid;

import com.whatsapp.authentication.dto.SignupRequest;
import com.whatsapp.authentication.services.SignupService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication/signup")
public class SignupController {
      private final SignupService signupService;
      
      @PostMapping
      @ResponseStatus(HttpStatus.CREATED)
      public void signup(@RequestBody @Valid SignupRequest signupRequest) {
            signupService.signup(signupRequest.toUser());
      }
}
