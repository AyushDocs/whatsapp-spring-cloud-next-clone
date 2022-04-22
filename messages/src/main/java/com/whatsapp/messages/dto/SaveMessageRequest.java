package com.whatsapp.messages.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveMessageRequest {
      @NotBlank
      @NotEmpty
      @NotNull
      private String content;
      @NotBlank
      @NotEmpty
      @NotNull
      private String sentBy;
       @NotBlank
      @NotEmpty
      @NotNull
      private String roomUuid;
}
