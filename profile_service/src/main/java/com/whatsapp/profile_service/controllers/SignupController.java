package com.whatsapp.profile_service.controllers;

import javax.validation.Valid;

import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class SignupController {
      private final AuthService authService;
      @PostMapping("/signup")
      @ResponseStatus(HttpStatus.CREATED)
      public void signup(@RequestBody @Valid SignupRequest signupRequest) {
            authService.signup(signupRequest.toUser());
      }
}
