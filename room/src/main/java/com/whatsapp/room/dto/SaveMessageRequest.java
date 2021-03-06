package com.whatsapp.room.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
