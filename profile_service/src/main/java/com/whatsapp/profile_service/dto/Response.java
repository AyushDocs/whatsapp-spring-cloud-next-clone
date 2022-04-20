package com.whatsapp.profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
      private T data;
      private boolean hasError;
}
