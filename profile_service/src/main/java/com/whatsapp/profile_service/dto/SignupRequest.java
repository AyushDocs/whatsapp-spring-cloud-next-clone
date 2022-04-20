package com.whatsapp.profile_service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.whatsapp.profile_service.models.User;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class SignupRequest {
      @Email
      @NotNull
      @NotBlank
      @NotEmpty
      private String email;
      @NotNull
      @NotBlank
      @NotEmpty
      private String password;
      @NotNull
      @NotBlank
      @NotEmpty
      private String username;
      public User toUser(){
            return User.builder()
                  .email(email)
                  .password(password)
                  .username(username)
                  .build();
      }
}
