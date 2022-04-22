package com.whatsapp.room.utils;

import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper {
      public static Room convertSaveRoomRequestToRoom(SaveRoomRequest request) {
            return new Room(
                        request.getName(),
                        "",
                        request.getImgUrl());
      }
}
