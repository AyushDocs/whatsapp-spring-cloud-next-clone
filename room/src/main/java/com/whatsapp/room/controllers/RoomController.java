package com.whatsapp.room.controllers;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.service.RoomService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class RoomController {
      private final RoomService roomService;
      @PostMapping("/rooms/")
      public void save(@RequestBody SaveRoomRequest saveRoomRequest) {
            roomService.saveRoom(saveRoomRequest);
      }
      @PostMapping("/messages/")
      public void save(@RequestBody SaveMessageRequest saveMessageRequest) {
            roomService.saveMessage(saveMessageRequest);
      }
      @GetMapping("/rooms/{userUuid}")
      public FindRoomsResponse[] findRoomsWithUnreadMessagesByUserUuid(@PathVariable String userUuid) {
            return roomService.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }
}
