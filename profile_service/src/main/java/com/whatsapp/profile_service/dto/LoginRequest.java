package com.whatsapp.profile_service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
      @Email
      @NotNull
      @NotBlank
      @NotEmpty
      private String email;
      @NotNull
      @NotBlank
      @NotEmpty
      private String password;
}
