package com.whatsapp.room.controllers;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.service.RoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomController {
      private final RoomService roomService;

      public void save(SaveRoomRequest saveRoomRequest) {
            roomService.saveRoom(saveRoomRequest);
      }

      public FindRoomsResponse[] findRoomsWithUnreadMessagesByUserUuid(String userUuid) {
            return roomService.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }
}
