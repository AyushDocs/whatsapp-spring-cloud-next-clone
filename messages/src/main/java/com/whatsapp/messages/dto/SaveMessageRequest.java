package com.whatsapp.messages.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveMessageRequest {
      @NotEmpty
      @NotBlank
      @NotNull
      private String content;
      @NotEmpty
      @NotBlank
      @NotNull
      private String sentByEmail;
      @NotEmpty
      @NotBlank
      @NotNull
      private String roomUuid;
      @NotNull
      private List<@NotEmpty @NotBlank @NotNull @Email String> unreadUsers;

}
