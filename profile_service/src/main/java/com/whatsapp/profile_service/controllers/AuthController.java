package com.whatsapp.profile_service.controllers;

import javax.validation.Valid;

import com.whatsapp.profile_service.config.JwtConfig;
import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.services.AuthService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AuthController {
      private final AuthService authService;
      private final JwtConfig jwtConfig;

      @PostMapping("/signup")
      @ResponseStatus(HttpStatus.CREATED)
      public void signup(@RequestBody @Valid SignupRequest signupRequest) {
            authService.signup(signupRequest.toUser());
      }

      @PostMapping("/login")
      public ResponseEntity<Void> login(
                  @RequestBody @Valid LoginRequest loginRequest) {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            String jwt = authService.login(email, password);
            
            return responseWithJwtCookie(jwt);
      }

      private ResponseEntity<Void> responseWithJwtCookie(String jwt) {
            ResponseCookie responseCookie = ResponseCookie.from(jwtConfig.getCookieName(), jwt).httpOnly(true)
                        .secure(true).build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
      }
}
