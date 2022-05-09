package com.whatsapp.room.service;

import static com.whatsapp.room.utils.Mapper.convertSaveRoomRequestToRoom;

import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.models.RoomUserId;
import com.whatsapp.room.repository.RoomRepository;
import com.whatsapp.room.repository.RoomUserIdRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomService {
      private final RoomRepository roomRepository;
      private final RoomUserIdRepository roomUserIdRepository;

      private final MessageService messageService;
      
      public void saveRoom(SaveRoomRequest saveRoomRequest) {
            Room room = convertSaveRoomRequestToRoom(saveRoomRequest);
            Room savedRoom = roomRepository.save(room);
            roomUserIdRepository.saveAll(saveRoomRequest
            .getUsers()
            .stream()
            .map(userUuid-> new RoomUserId(null, savedRoom.getUuid(),userUuid))
            .toList())
            ;

      }

      public List<FindRoomsResponse> findRoomsWithUnreadMessagesByUserUuid(String userUuid) {
            return roomRepository.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }

      public void saveMessage(SaveMessageRequest saveMessageRequest) {
            roomRepository.saveMessage(saveMessageRequest.getRoomUuid(), saveMessageRequest.getContent());
            // messageService.sendMessage(saveMessageRequest);
      }

}
