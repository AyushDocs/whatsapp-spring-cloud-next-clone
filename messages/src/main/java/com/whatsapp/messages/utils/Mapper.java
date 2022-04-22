package com.whatsapp.messages.utils;

import static lombok.AccessLevel.PRIVATE;

import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.models.Message.Status;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class Mapper {
      public static Message convertRequestMessageToMessageEntity(SaveMessageRequest request) {
            return new Message(
                        request.getContent(),
                        request.getSentBy(),
                        Status.RECEIVED_BY_SERVER,
                        request.getRoomUuid());
      }
}
