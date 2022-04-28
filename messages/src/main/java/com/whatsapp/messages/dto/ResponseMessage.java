package com.whatsapp.messages.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseMessage {
      private String content;
      private LocalDateTime createdAt;
      private String sentByEmail;
}
