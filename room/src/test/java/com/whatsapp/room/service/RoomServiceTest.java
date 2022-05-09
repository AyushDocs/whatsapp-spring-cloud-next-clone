package com.whatsapp.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.repository.RoomRepository;
import com.whatsapp.room.repository.RoomUserIdRepository;

import org.junit.Before;
import org.junit.Test;
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
      @Mock
      private RoomUserIdRepository roomUserIdRepository;
      @Mock
      private MessageService messageService;
      private RoomService underTest;

      @Before
      public void setup() {
            underTest = new RoomService(roomRepository,null, messageService);
      }

      @Test
      public void should_not_find_rooms_no_user_with_given_uuid() {
            LocalDateTime now = LocalDateTime.now();
            FindRoomsResponse res = new FindRoomsResponse("roomUuid",
                        "name",
                        "",
                        now);
            List<FindRoomsResponse> resArr = Arrays.asList(res);
            when(roomRepository.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(resArr);

            List<FindRoomsResponse> resArrFromMethod = underTest
                        .findRoomsWithUnreadMessagesByUserUuid("userUuid");

            verify(roomRepository).findRoomsWithUnreadMessagesByUserUuid(anyString());
            assertEquals("roomUuid", resArrFromMethod.get(0).getRoomUuid());
            assertEquals("name", resArrFromMethod.get(0).getRoomName());
            assertEquals("", resArrFromMethod.get(0).getLastMessage());
            assertEquals(now, resArrFromMethod.get(0).getTimestamp());
      }

      @Test
      public void should_save_room() {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest("name",
                        "imgUrl",
                        List.of("userId1", "userId2"));

            underTest.saveRoom(saveRoomRequest);
            ArgumentCaptor<Room> ac = ArgumentCaptor.forClass(Room.class);
            verify(roomRepository).save(ac.capture());
            Room room = ac.getValue();
            assertEquals("name", room.getName());
            assertEquals("", room.getLastMessage());
            assertEquals("imgUrl", room.getImgUrl());
      }

      @Test
      public void should_change_room_last_message() {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest(
                        "content",
                        "sentBy",
                        "roomUuid");

            underTest.saveMessage(saveMessageRequest);
            verify(messageService).sendMessage(saveMessageRequest);
            verify(roomRepository).saveMessage(anyString(), anyString());
      }
}
