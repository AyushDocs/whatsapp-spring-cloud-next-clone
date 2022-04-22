package com.whatsapp.room.service;

import static com.whatsapp.room.utils.Mapper.convertSaveRoomRequestToRoom;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.repository.RoomRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomService {
      private final RoomRepository roomRepository;
      public void saveRoom(SaveRoomRequest saveRoomRequest) {
            Room room = convertSaveRoomRequestToRoom(saveRoomRequest);
            roomRepository.save(room);
      }

      public FindRoomsResponse[] findRoomsWithUnreadMessagesByUserUuid(String userUuid) {
            return roomRepository.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }

      public void saveMessage(SaveMessageRequest saveMessageRequest) {
            roomRepository.saveMessage(saveMessageRequest.getRoomUuid(), saveMessageRequest.getContent());
            //TODO:send request to message service later
      }

}
