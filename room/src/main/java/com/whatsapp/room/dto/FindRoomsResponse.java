package com.whatsapp.room.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindRoomsResponse {
      private String roomUuid;
      private String roomName;
      private String lastMessage;
      private LocalDateTime timestamp;
}
