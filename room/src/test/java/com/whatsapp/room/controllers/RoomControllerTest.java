package com.whatsapp.room.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.service.RoomService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(RoomController.class)
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
      @Autowired
      private RoomController controller;
      @MockBean
      private RoomService roomService;

      @Test
      void should_save_room() {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        "name",
                        "http://localhost:8080/api/v1/images",
                        List.of("ayushUuid"));
            controller.save(saveRoomRequest);
            verify(roomService).saveRoom(any(SaveRoomRequest.class));
      }

      @Test
      void should_find_rooms() {
            LocalDateTime now = LocalDateTime.now();
            FindRoomsResponse findRoomsResponse = new FindRoomsResponse("roomUuid",
                        "roomName",
                        "lastMessage",
                        now);
            List<FindRoomsResponse> responseArr = Arrays.asList(findRoomsResponse);
            when(roomService.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(responseArr);

            List<FindRoomsResponse> response = controller
                        .findRoomsWithUnreadMessagesByUserUuid("uuid");

            verify(roomService).findRoomsWithUnreadMessagesByUserUuid(anyString());
            assertEquals("roomUuid", response.get(0).getRoomUuid());
            assertEquals("roomName", response.get(0).getRoomName());
            assertEquals("lastMessage", response.get(0).getLastMessage());
            assertEquals(now, response.get(0).getTimestamp());
      }
      @Test
      void should_update_room_last_message() {
            controller.save(new SaveMessageRequest("content", "sentBy", "roomUuid"));
            verify(roomService).saveMessage(any(SaveMessageRequest.class));
      }
}