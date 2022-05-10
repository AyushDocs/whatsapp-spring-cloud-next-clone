package com.whatsapp.room.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.whatsapp.library.Response;
import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.dto.SaveRoomResponse;
import com.whatsapp.room.service.RoomService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class RoomController {
      private final RoomService roomService;
      @PostMapping("/rooms/")
      @ResponseStatus(CREATED)
      public Response<SaveRoomResponse> save(@RequestBody @Valid SaveRoomRequest saveRoomRequest) {
            return roomService.saveRoom(saveRoomRequest);
      }
      @PutMapping("/rooms/")
      public void save(@RequestBody @Valid SaveMessageRequest saveMessageRequest) {
            roomService.saveMessage(saveMessageRequest);
      }
      @GetMapping("/rooms/{userUuid}")
      public List<FindRoomsResponse> findRoomsWithUnreadMessagesByUserUuid(@PathVariable String userUuid) {
            return roomService.findRoomsWithUnreadMessagesByUserUuid(userUuid);
      }
}
