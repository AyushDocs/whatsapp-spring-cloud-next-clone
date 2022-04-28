package com.whatsapp.messages.adapters;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.repositories.MessageRepository;
import com.whatsapp.messages.repositories.MessageUserIdRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
      private MessageService underTest;
      @Mock
      private MessageRepository messageRepo;
      @Mock
      private MessageUserIdRepository messageUserIdRepo;
      @Before
      public void setup() {
            underTest = new MessageService(messageRepo, messageUserIdRepo);
      }

      @Test
      public void should_save_message() {
            SaveMessageRequest messageRequest = new SaveMessageRequest(
                        "content",
                        "sentBy",
                        "roomUuid",
                        List.of("user1", "user1"));

            underTest.saveMessage(messageRequest);

            verify(messageRepo).saveAndFlush(any(Message.class));
            verify(messageUserIdRepo).saveAll(any(List.class));
      }
      @Test
      public void should_find_unread_messages() {
            ResponseMessage res=new ResponseMessage("content",now(),"sender@g.com");
            List<ResponseMessage> messages=List.of(res);
            when(messageRepo.findUnreadMessages(anyString(), anyString())).thenReturn(messages);

            List<ResponseMessage> response = underTest.findUnreadMessages("myEmail","roomUuid");
            
            assertEquals(messages, response);
            verify(messageRepo).findUnreadMessages(anyString(), anyString());
      }
}
