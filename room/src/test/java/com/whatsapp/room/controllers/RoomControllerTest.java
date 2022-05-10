package com.whatsapp.room.controllers;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.library.Response;
import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.dto.SaveRoomResponse;
import com.whatsapp.room.service.RoomService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(RoomController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureJson
class RoomControllerTest {
      @Autowired
      private RoomController controller;
      @Autowired
      private MockMvc mvc;
      @Autowired
      private ObjectMapper mapper;
      @MockBean
      private RoomService roomService;

      @Test
      void controller_should_be_present() throws JsonProcessingException, Exception {
            assertNotNull(controller);
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void should_not_save_room(String text) throws JsonProcessingException, Exception {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        text,
                        text,
                        asList(text));
            MockHttpServletRequestBuilder request = post("/api/v1/rooms/")
                        .content(mapper.writeValueAsString(saveRoomRequest))
                        .contentType(APPLICATION_JSON);

            ResultActions result = mvc.perform(request);
            result.andExpect(status().isUnprocessableEntity());
            result.andExpect(jsonPath("$.data.name").exists());
            result.andExpect(jsonPath("$.data.imgUrl").exists());
            result.andExpect(jsonPath("$.data.users").exists());
      }

      @Test
      void should_save_room() throws JsonProcessingException, Exception {
            Response<SaveRoomResponse> res=new Response<>(new SaveRoomResponse("uuid","name","","http://localhost:8080/api/v1/images",LocalDateTime.now()),false);
            when(roomService.saveRoom(any(SaveRoomRequest.class))).thenReturn(res);
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        "name",
                        "http://localhost:8080/api/v1/images",
                        List.of("ayushUuid", "heUuid"));
            MockHttpServletRequestBuilder request = post("/api/v1/rooms/")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(saveRoomRequest));

            ResultActions result = mvc.perform(request);

            verify(roomService).saveRoom(any(SaveRoomRequest.class));
            result.andExpect(status().isCreated());
            result.andExpect(jsonPath("$.data.name").value("name"));
            result.andExpect(jsonPath("$.data.imgUrl").value("http://localhost:8080/api/v1/images"));
            result.andExpect(jsonPath("$.data.uuid").exists());
            result.andExpect(jsonPath("$.data.updatedAt").exists());
            result.andExpect(jsonPath("$.data.lastMessage").exists());
      }

      @Test
      void should_find_rooms() throws Exception {
            LocalDateTime now = LocalDateTime.now();
            FindRoomsResponse findRoomsResponse = new FindRoomsResponse("roomUuid",
                        "roomName",
                        "lastMessage",
                        "http://localhost:8080/api/v1/images",
                        now);
            List<FindRoomsResponse> responseArr = asList(findRoomsResponse);
            when(roomService.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(responseArr);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .get("/api/v1/rooms/roomUuid");

            ResultActions result = mvc.perform(request);

            verify(roomService).findRoomsWithUnreadMessagesByUserUuid(anyString());
            result.andExpect(status().isOk());
            result.andExpect(
                        content().json("[" + mapper.writeValueAsString(findRoomsResponse) + "]"));
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void should_not_update_room_last_message(String text) throws JsonProcessingException, Exception {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest(text, text, text);

            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/v1/rooms/")
                        .content(mapper.writeValueAsString(saveMessageRequest))
                        .contentType(APPLICATION_JSON);
            ResultActions result = mvc.perform(request);

            verify(roomService, times(0)).saveMessage(any(SaveMessageRequest.class));
            result.andExpect(status().isUnprocessableEntity());
            result.andExpect(jsonPath("$.data.roomUuid").exists());
            result.andExpect(jsonPath("$.data.content").exists());
            result.andExpect(jsonPath("$.data.sentBy").exists());
      }

      @Test
      void should_update_room_last_message() throws Exception {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest("content", "sentBy", "roomUuid");

            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/v1/rooms/")
                        .content(mapper.writeValueAsString(saveMessageRequest))
                        .contentType(APPLICATION_JSON);
            ResultActions result = mvc.perform(request);

            verify(roomService).saveMessage(any(SaveMessageRequest.class));
            result.andExpect(status().isOk());
      }
}