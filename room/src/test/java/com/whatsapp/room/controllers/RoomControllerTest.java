package com.whatsapp.room.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.service.RoomService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(RoomController.class)
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
      private RoomController controller;
      @Mock
      private RoomService roomService;

      @BeforeEach
      void setup() {
            controller = new RoomController(roomService);
      }

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
            FindRoomsResponse[] responseArr = { findRoomsResponse};
            when(roomService.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(responseArr);

            FindRoomsResponse[] response = controller
                        .findRoomsWithUnreadMessagesByUserUuid("uuid");

            verify(roomService).findRoomsWithUnreadMessagesByUserUuid(anyString());
            assertEquals("roomUuid", response[0].getRoomUuid());
            assertEquals("roomName", response[0].getRoomName());
            assertEquals("lastMessage", response[0].getLastMessage());
            assertEquals(now, response[0].getTimestamp());
      }
}