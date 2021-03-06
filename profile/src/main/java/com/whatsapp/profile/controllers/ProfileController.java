package com.whatsapp.profile.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.whatsapp.library.Response;
import com.whatsapp.profile.dto.AddImageUrlDto;
import com.whatsapp.profile.dto.FindUserDto;
import com.whatsapp.profile.dto.SaveUserDto;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.exceptions.InvalidInputException;
import com.whatsapp.profile.service.ProfileService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Validated
public class ProfileController {
      private final ProfileService userService;

      @GetMapping
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

      @PostMapping
      @ResponseStatus(CREATED)
      public void saveUser(@RequestBody @Valid SaveUserDto saveUserDto) {
            userService.saveUser(saveUserDto);
      }
      @PutMapping
      public void addImageUrlForUser(@RequestBody @Valid AddImageUrlDto addImageUrlDto) {
            userService.updateImageUrl(addImageUrlDto);
      }

      @GetMapping("/{email}")
      public FindUserDto findByEmail(@PathVariable @NotBlank @NotNull @NotEmpty @Email String email) {
            return userService.findByEmail(email);
      }
}