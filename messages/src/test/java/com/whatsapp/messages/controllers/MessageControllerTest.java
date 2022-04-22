package com.whatsapp.messages.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import com.whatsapp.messages.controllers.MessageController;
import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.service.MessageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@WebMvcTest(MessageController.class)
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class MessageControllerTest {
      private MessageController controller;
      @Mock
      private MessageService messageService;

      @BeforeEach
      void setup() {
            controller = new MessageController(messageService);
      }

      @Test
      void should_save_message() {
            
            SaveMessageRequest saveRoomRequest = new SaveMessageRequest(
                        "content",
                        "sentBy",
                        "roomUuid");
            controller.save(saveRoomRequest);
            verify(messageService).saveMessage(any(SaveMessageRequest.class));
      }

      @Test
      void should_find_rooms() {
            LocalDateTime now = LocalDateTime.now();
            FindMessageResponse findRoomsResponse = new FindMessageResponse("content",
                        "sentBy",
                        now);
            FindMessageResponse[] responseArr = { findRoomsResponse };
            when(messageService.findUnreadMessages(anyString(),anyString()))
                        .thenReturn(responseArr);

            FindMessageResponse[] response = controller
                        .findRoomsWithUnreadMessagesByUserUuid("uuid","uuid");

            verify(messageService).findUnreadMessages("uuid", "uuid");
            assertEquals("content", response[0].getContent());
            assertEquals("sentBy", response[0].getSentBy());
            assertEquals(now, response[0].getTimestamp());
      }
}