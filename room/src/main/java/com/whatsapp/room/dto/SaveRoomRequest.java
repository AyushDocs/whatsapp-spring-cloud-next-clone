package com.whatsapp.room.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRoomRequest {
      private String name;
      private String imgUrl;
      private List<String> users;
}
