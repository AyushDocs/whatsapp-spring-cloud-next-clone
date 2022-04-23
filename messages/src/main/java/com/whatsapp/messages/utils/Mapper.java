package com.whatsapp.messages.utils;

import static com.whatsapp.messages.models.MessageStatus.RECEIVED_BY_SERVER;
import static lombok.AccessLevel.PRIVATE;

import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class Mapper {
      public static Message convertRequestMessageToMessageEntity(SaveMessageRequest request) {
            return new Message(
                        request.getContent(),
                        request.getSentBy(),
                        RECEIVED_BY_SERVER,
                        request.getRoomUuid());
      }
}
