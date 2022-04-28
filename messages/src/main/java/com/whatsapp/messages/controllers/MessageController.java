package com.whatsapp.messages.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.whatsapp.messages.adapters.MessageService;
import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.dto.SaveMessageRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class MessageController {
      private final MessageService messageService;

      @PostMapping("/api/v1/messages")
      @ResponseStatus(CREATED)
      public void save(@RequestBody @Valid SaveMessageRequest saveMessageRequest){
            messageService.saveMessage(saveMessageRequest);
      }
      @GetMapping("/api/v1/messages")
      @ResponseStatus(PARTIAL_CONTENT)
      public List<ResponseMessage> findUnreadMessages(
            @RequestParam @NotNull @NotBlank @NotEmpty @Email String email,
            @RequestParam @NotNull @NotBlank @NotEmpty String roomUuid){
            return messageService.findUnreadMessages(email, roomUuid); 
      }
}
