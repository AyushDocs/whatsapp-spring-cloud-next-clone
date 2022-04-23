package com.whatsapp.room.service;

import static com.whatsapp.room.utils.Mapper.convertSaveRoomRequestToRoom;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.repository.RoomRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomService {
      private final RoomRepository roomRepository;
      private final RestTemplate restTemplate;

      private static final String MESSAGE_SERVICE_URL = "http://MESSAGE-SERVICE";
      
      public void saveRoom(SaveRoomRequest saveRoomRequest) {
            Room room = convertSaveRoomRequestToRoom(saveRoomRequest);
            roomRepository.save(room);
      }

      public FindRoomsResponse[] findRoomsWithUnreadMessagesByUserUuid(String userUuid) {
            return roomRepository.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }

      public void saveMessage(SaveMessageRequest saveMessageRequest) {
            roomRepository.saveMessage(saveMessageRequest.getRoomUuid(), saveMessageRequest.getContent());
            sendMessageToMessageService(saveMessageRequest);
      }

      private void sendMessageToMessageService(SaveMessageRequest saveMessageRequest) {
            restTemplate.postForEntity(MESSAGE_SERVICE_URL, saveMessageRequest, Void.class);
      }

}
