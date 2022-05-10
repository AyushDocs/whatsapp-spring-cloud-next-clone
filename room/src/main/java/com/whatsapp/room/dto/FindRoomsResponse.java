package com.whatsapp.room.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindRoomsResponse {
      private String roomName;
      private String roomUuid;
      private String lastMessage;
      private String imgUrl;
      private LocalDateTime timestamp;
}
