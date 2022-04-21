package com.whatsapp.profile_service.controllers;

import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.dto.UserResponseDto;
import com.whatsapp.profile_service.exceptions.InvalidInputException;
import com.whatsapp.profile_service.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController{
      private final UserService userService;

      @GetMapping("/")
      @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
      public Response<Page<UserResponseDto>> a(@RequestParam String text,
                                           @RequestParam(defaultValue = "0") int offset,
                                           @RequestParam(defaultValue = "10") int limit){
            if(text.isEmpty()||text.isBlank())throw new InvalidInputException("text is either blank or empty");
            Page<UserResponseDto> users = userService.findUsersByText(text, offset, limit);
            return new Response<>(users,false);
      }
}