package com.whatsapp.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.whatsapp.room.models.Room;
import com.whatsapp.room.repository.RoomRepository;
import com.whatsapp.room.repository.RoomUserIdRepository;
import com.whatsapp.room.repository.UnreadRoomUserRepository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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
      private UnreadRoomUserRepository unreadRoomUserRepository;
      @Mock
      private MessageService messageService;
      @InjectMocks
      private RoomService underTest;

      @Test
      public void should_not_find_rooms_no_user_with_given_uuid() {
            LocalDateTime now = LocalDateTime.now();
            FindRoomsResponse res = new FindRoomsResponse("name","uuid","",
                        "imgUrl",
                        now);
            List<FindRoomsResponse> resArr = Arrays.asList(res);
            when(roomRepository.findRoomsWithUnreadMessagesByUserUuid(anyString()))
                        .thenReturn(resArr);

            List<FindRoomsResponse> resArrFromMethod = underTest
                        .findRoomsWithUnreadMessagesByUserUuid("userUuid");

            verify(roomRepository).findRoomsWithUnreadMessagesByUserUuid(anyString());
            assertEquals("", resArrFromMethod.get(0).getLastMessage());
            assertEquals(now, resArrFromMethod.get(0).getTimestamp());
            assertEquals("name", resArrFromMethod.get(0).getRoomName());
            assertNotNull(resArrFromMethod.get(0).getRoomUuid());
      }

      @Test
      public void should_save_room() {
            Room roomToSave=new Room("name","lastMessage","imgUrl");
            roomToSave.setId(1L);
            roomToSave.setUuid("roomUuid");
            when(roomRepository.save(any(Room.class))).thenReturn(roomToSave);
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
      @SuppressWarnings("unchecked")
      public void should_change_room_last_message() {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest(
                        "content",
                        "sentBy",
                        "roomUuid");

            underTest.saveMessage(saveMessageRequest);
            verify(messageService).sendMessage(saveMessageRequest);
            verify(unreadRoomUserRepository).saveAll(any(Iterable.class));
            verify(roomRepository).updateMessage(anyString(), anyString());
      }
}
