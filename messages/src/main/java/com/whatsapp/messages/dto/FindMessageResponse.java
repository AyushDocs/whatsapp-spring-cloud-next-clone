package com.whatsapp.messages.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindMessageResponse {
      @NotBlank
      @NotEmpty
      @NotNull
      private String content;
      @NotBlank
      @NotEmpty
      @NotNull
      private String sentBy;
      @NotNull
      private LocalDateTime timestamp;
}
