package com.whatsapp.messages.controllers;

import javax.validation.Valid;

import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.service.MessageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/messages/")
public class MessageController {
      private final MessageService messageService;

      @PostMapping("/")
      public void save(@RequestBody @Valid SaveMessageRequest saveRoomRequest) {
            messageService.saveMessage(saveRoomRequest);
      }

      @GetMapping("/{roomId}/{userUuid}")
      public FindMessageResponse[] findRoomsWithUnreadMessagesByUserUuid(
                  @PathVariable String roomId,
                  @PathVariable String userUuid) {
            return messageService.findUnreadMessages(roomId, userUuid);
      }
}
