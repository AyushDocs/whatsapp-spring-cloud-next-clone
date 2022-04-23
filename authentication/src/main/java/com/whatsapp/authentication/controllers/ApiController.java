package com.whatsapp.authentication.controllers;

import com.whatsapp.authentication.utils.JwtUtils;
import com.whatsapp.library.Response;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ApiController {
      private final JwtUtils jwtUtils;
      @PostMapping("/getRoles")
      public Response<String[]> getRoles(@CookieValue("token")String jwt) {
            if(jwt.isEmpty()||jwt.isBlank())throw new IllegalArgumentException();
            String[] data = jwtUtils.extractRoles(jwt);
            return new Response<>(data, false);
      }
}