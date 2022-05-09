package com.whatsapp.room.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRoomRequest {
      @NotBlank
      @NotEmpty
      @NotNull
      private String name;
      @NotBlank
      @NotEmpty
      @NotNull
      @URL
      private String imgUrl;
      @Size(min = 2, max = 10)
      @NotNull
      private List<@NotBlank @NotEmpty @NotNull String> users;
}
