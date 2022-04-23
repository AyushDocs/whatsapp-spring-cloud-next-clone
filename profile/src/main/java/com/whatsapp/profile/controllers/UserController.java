package com.whatsapp.profile.controllers;

import com.whatsapp.library.Response;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.exceptions.InvalidInputException;
import com.whatsapp.profile.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserController {
      private final UserService userService;

      @GetMapping("/")
      @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
      public Response<Page<UserResponseDto>> findPossibleFriends(
                  @RequestParam String text,
                  @RequestParam(defaultValue = "0") int offset,
                  @RequestParam(defaultValue = "10") int limit) {
            validateText(text);
            Page<UserResponseDto> users = userService.findUsersByText(text, offset, limit);
            return new Response<>(users, false);
      }

      private void validateText(String text) {
            if (text.isEmpty() || text.isBlank())
                  throw new InvalidInputException("text is either blank or empty");
      }
}