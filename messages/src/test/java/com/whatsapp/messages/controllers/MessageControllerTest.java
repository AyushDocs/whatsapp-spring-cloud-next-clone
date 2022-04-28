package com.whatsapp.messages.controllers;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.messages.adapters.MessageService;
import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.dto.SaveMessageRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MessageController.class)
@AutoConfigureJson
class MessageControllerTest {
      @Autowired
      private MockMvc mvc;
      @Autowired
      private MessageController controller;
      @MockBean
      private MessageService messageService;
      @Autowired
      private ObjectMapper mapper;
      private static final String CONTENT = "content";
      private static final String SENT_BY = "sentBy";
      private static final String ROOM_UUID = "roomUuid";
      private static final List<String> UNREAD_USERS = List.of("a@g.com");

      @Test
      void check_for_controller_bean() throws Exception {
           assertNotNull(controller);
      }
      @Test
      void should_not_send_message_no_content_type() throws Exception {
            ResultActions result = mvc.perform(post("/api/v1/messages"));

            result.andExpect(status().isBadRequest());
      }

      @Test
      void should_not_send_message_no_content() throws Exception {
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON));

            result .andExpect(status().isBadRequest());
      }
      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings={" "})
      void should_not_send_message_no_content_in_body(String content) throws Exception {
            SaveMessageRequest request = new SaveMessageRequest(content,SENT_BY,ROOM_UUID, UNREAD_USERS);
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)));

            result.andExpect(status().isBadRequest());
      }
      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings={" "})
      void should_not_send_message_no_sentBy_in_body(String sentBy) throws Exception {
            SaveMessageRequest request = new SaveMessageRequest(CONTENT,sentBy,ROOM_UUID, UNREAD_USERS);
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)));

            result.andExpect(status().isBadRequest());
      }
      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings={" "})
      void should_not_send_message_no_room_uuid_in_body(String roomUuid) throws Exception {
            SaveMessageRequest request = new SaveMessageRequest(CONTENT,SENT_BY,roomUuid, UNREAD_USERS);
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)));

            result.andExpect(status().isBadRequest());
      }
      @ParameterizedTest
      @NullSource
      @ValueSource(strings={" ","ag.com","agcom"})
      void should_not_send_message_unread_users_in_body(String email) throws Exception {
            List<String> emails = Arrays.asList(new String[]{email});
            SaveMessageRequest request = new SaveMessageRequest(CONTENT,SENT_BY,ROOM_UUID,emails);
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)));

            result.andExpect(status().isBadRequest());
      }
      @Test
      void should_send_message() throws Exception {
            SaveMessageRequest request = new SaveMessageRequest(CONTENT,SENT_BY,ROOM_UUID,UNREAD_USERS);
            ResultActions result = mvc.perform(post("/api/v1/messages")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)));
            result.andExpect(status().isCreated());

            verify(messageService).saveMessage(any(SaveMessageRequest.class)); 
      }
      //--------------------------------------------------------------------------------------------------------------------
      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings={" "})
      void should_not_get_messages_email(String email) throws Exception {
            ResultActions result = mvc.perform(get("/api/v1/messages?email=%s&roomUuid=hhhy".formatted(email)));
            
            result.andExpect(status().isBadRequest());
      }
      @ParameterizedTest
      @EmptySource
      @ValueSource(strings={" "})
      void should_not_get_messages_roomUuid(String roomUuid) throws Exception {
            ResultActions result = mvc.perform(get("/api/v1/messages?email=a@g.coms&roomUuid="+roomUuid));
            
            result.andExpect(status().isBadRequest());
      }
      @Test
      void should_find_messages() throws Exception {
            ResponseMessage res=new ResponseMessage("content",now(),"sender@g.com");
            List<ResponseMessage> messages=List.of(res);
            when(messageService.findUnreadMessages(anyString(),anyString())).thenReturn(messages);

            ResultActions result = mvc.perform(get("/api/v1/messages?email=a@g.coms&roomUuid=79802"));
            
            result.andExpect(status().isPartialContent());
            result.andExpect(content().contentType(APPLICATION_JSON));
            result.andExpect(content().json(asJsonString(messages)));
            verify(messageService).findUnreadMessages(anyString(),anyString()); 
      }

      private String asJsonString(Object object) throws JsonProcessingException {
            return mapper.writeValueAsString(object);
      }

}
