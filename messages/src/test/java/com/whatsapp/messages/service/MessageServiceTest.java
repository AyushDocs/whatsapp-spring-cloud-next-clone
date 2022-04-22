package com.whatsapp.messages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.repository.MessageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
      @Mock
      private MessageRepository messageRepository;
      private MessageService messageService;

      @Before
      public void setup() {
            messageService = new MessageService(messageRepository);
      }

      @Test
      public void should_find_messages() {
            LocalDateTime now = LocalDateTime.now();
            FindMessageResponse res=new FindMessageResponse("content","sentBy",now);
            FindMessageResponse[] messages={res};
            when(messageRepository.findUnreadMessages(anyString(), anyString()))
                  .thenReturn(messages);

            FindMessageResponse[] result = messageService
                        .findUnreadMessages("roomId", "userUuid");

            assertEquals("content", result[0].getContent());
            assertEquals("sentBy", result[0].getSentBy());
            assertEquals(now, result[0].getTimestamp());
      }

      @Test
      public void should_save_message() {
            SaveMessageRequest request=new SaveMessageRequest("content","sentBy","roomUuid");
            messageService.saveMessage(request);
            verify(messageRepository).save(any(Message.class));
      }
}
