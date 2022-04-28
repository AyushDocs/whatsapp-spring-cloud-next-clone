package com.whatsapp.messages.adapters;

import java.util.List;

import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.models.MessageUserId;
import com.whatsapp.messages.repositories.MessageRepository;
import com.whatsapp.messages.repositories.MessageUserIdRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageService {
      private final MessageRepository messageRepository;
      private final MessageUserIdRepository messageUserIdRepo;

      public void saveMessage(SaveMessageRequest saveMessageRequest) {
            saveMessageInDb(saveMessageRequest);
            List<String> unreadUsers = saveMessageRequest.getUnreadUsers();
            messageUserIdRepo.saveAll(
                  unreadUsers.stream()
                  .map(user->new MessageUserId(null,user,saveMessageRequest.getRoomUuid()))
                  .toList()
            );

      }

      private void saveMessageInDb(SaveMessageRequest saveMessageRequest) {
            String content = saveMessageRequest.getContent();
            String roomUuid = saveMessageRequest.getRoomUuid();
            String sentByEmail = saveMessageRequest.getSentByEmail();
            Message message = new Message(content, roomUuid, sentByEmail);
            messageRepository.saveAndFlush(message);
      }

      public List<ResponseMessage> findUnreadMessages(String userEmail, String roomUuid) {
            return messageRepository.findUnreadMessages(userEmail, roomUuid);
      }

}
