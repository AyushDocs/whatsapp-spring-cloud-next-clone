package com.whatsapp.room.service;

import static com.whatsapp.room.utils.Mapper.convertSaveRoomRequestToRoom;

import java.util.List;

import com.whatsapp.library.Response;
import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.dto.SaveRoomResponse;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.models.RoomUserId;
import com.whatsapp.room.models.UnreadRoomUser;
import com.whatsapp.room.repository.RoomRepository;
import com.whatsapp.room.repository.RoomUserIdRepository;
import com.whatsapp.room.repository.UnreadRoomUserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomService {
      private final RoomRepository roomRepository;
      private final RoomUserIdRepository roomUserIdRepository;
      private final UnreadRoomUserRepository unreadRoomUserRepository;

      private final MessageService messageService;

      public Response<SaveRoomResponse> saveRoom(SaveRoomRequest saveRoomRequest) {
            Room room = convertSaveRoomRequestToRoom(saveRoomRequest);
            Room savedRoom = roomRepository.save(room);
            roomUserIdRepository.saveAll(saveRoomRequest
                        .getUsers()
                        .stream()
                        .map(userUuid -> new RoomUserId(null, savedRoom.getUuid(), userUuid))
                        .toList());
            SaveRoomResponse response = SaveRoomResponse.builder()
            .imgUrl(savedRoom.getImgUrl())
            .lastMessage(savedRoom.getImgUrl())
            .name(savedRoom.getName())
            .updatedAt(savedRoom.getUpdatedAt())
            .uuid(savedRoom.getUuid())
            .build();
            return new Response<>(response,false);
      }

      public List<FindRoomsResponse> findRoomsWithUnreadMessagesByUserUuid(String userUuid) {
            return roomRepository.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }

      public void saveMessage(SaveMessageRequest saveMessageRequest) {
            String roomUuid = saveMessageRequest.getRoomUuid();
            roomRepository.updateMessage(roomUuid, saveMessageRequest.getContent());
            List<UnreadRoomUser> unreadRoomUsers = roomUserIdRepository
                        .findUserUuidByRoomUuid(saveMessageRequest.getRoomUuid())
                        .stream()
                        .map(userUuid -> new UnreadRoomUser(null, roomUuid, userUuid))
                        .toList();

            unreadRoomUserRepository
                        .saveAll(unreadRoomUsers);
            messageService.sendMessage(saveMessageRequest);
      }

}
