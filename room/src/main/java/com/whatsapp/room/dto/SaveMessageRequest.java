package com.whatsapp.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveMessageRequest {
      private String content;
      private String sentBy;
      private String roomUuid;
}
