package com.whatsapp.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.repository.RoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
      @Mock
      private RoomRepository roomRepository;
      private RoomService roomService;

      @BeforeEach
      void setup() {
            roomService = new RoomService(roomRepository);
      }

      @Test
      void should_not_find_rooms_no_user_with_given_uuid() {
            LocalDateTime now = LocalDateTime.now();
            FindRoomsResponse res = new FindRoomsResponse("roomUuid",
                        "name",
                        "",
                        now);
            FindRoomsResponse[] resArr = { res };
            when(roomRepository.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(resArr);

            FindRoomsResponse[] resArrFromMethod = roomService
                        .findRoomsWithUnreadMessagesByUserUuid("userUuid");

            verify(roomRepository).findRoomsWithUnreadMessagesByUserUuid(anyString());
            assertEquals("roomUuid", resArrFromMethod[0].getRoomUuid());
            assertEquals("name", resArrFromMethod[0].getRoomName());
            assertEquals("", resArrFromMethod[0].getLastMessage());
            assertEquals(now, resArrFromMethod[0].getTimestamp());
      }

      @Test
      void should_save_room() {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest("name",
                        "imgUrl",
                        List.of("userId1", "userId2"));

            roomService.saveRoom(saveRoomRequest);
            ArgumentCaptor<Room> ac = ArgumentCaptor.forClass(Room.class);
            verify(roomRepository).save(ac.capture());
            Room room = ac.getValue();
            assertEquals("name", room.getName());
            assertEquals("", room.getLastMessage());
            assertEquals("imgUrl", room.getImgUrl());
      }
}
