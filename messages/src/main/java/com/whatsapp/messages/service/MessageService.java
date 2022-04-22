package com.whatsapp.messages.service;

import static com.whatsapp.messages.utils.Mapper.convertRequestMessageToMessageEntity;

import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.repository.MessageRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageService {
      private final MessageRepository messageRepository;

      public void saveMessage(SaveMessageRequest saveRoomRequest) {
            Message message = convertRequestMessageToMessageEntity(saveRoomRequest);
            messageRepository.save(message);
      }

      public FindMessageResponse[] findUnreadMessages(String roomId,String userUuid) {
            return messageRepository.findUnreadMessages(roomId,userUuid);
      }

}
