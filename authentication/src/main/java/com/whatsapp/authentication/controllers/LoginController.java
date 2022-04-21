package com.whatsapp.authentication.controllers;

import javax.validation.Valid;

import com.whatsapp.authentication.config.JwtConfig;
import com.whatsapp.authentication.dto.LoginRequest;
import com.whatsapp.authentication.services.LoginService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/login")
public class LoginController {
      private final LoginService loginService;
      private final JwtConfig jwtConfig;
      @PostMapping
      public ResponseEntity<Void> login(
                  @RequestBody @Valid LoginRequest loginRequest) {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            String jwt = loginService.login(email, password);

            return responseWithJwtCookie(jwt);
      }

      private ResponseEntity<Void> responseWithJwtCookie(String jwt) {
            ResponseCookie responseCookie = ResponseCookie.from(jwtConfig.getCookieName(), jwt).httpOnly(true)
                        .secure(true).build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
      }
}
