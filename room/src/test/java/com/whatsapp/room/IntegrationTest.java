package com.whatsapp.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

import java.util.List;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.models.RoomUserId;
import com.whatsapp.room.repository.RoomRepository;
import com.whatsapp.room.repository.RoomUserIdRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class IntegrationTest {
      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired
      private RoomRepository roomRepo;
      @Autowired
      private RoomUserIdRepository roomUserIdRepo;

      @Test
      void should_save_room() {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        "name",
                        "http://localhost:8080/api/v1/images",
                        List.of("ayushUuid"));

            ResponseEntity<Void> res = restTemplate.postForEntity("/api/v1/rooms/",
                        saveRoomRequest,
                        Void.class);
            assertEquals(200, res.getStatusCodeValue());
      }

      @Test
      void should_find_rooms() {
            Room savedRoom = roomRepo.save(new Room("roomName", "lastMessage", "img_url"));
            roomUserIdRepo.save(new RoomUserId(null, savedRoom.getUuid(), "userId"));
            ResponseEntity<FindRoomsResponse[]> res = restTemplate.exchange("/api/v1/rooms/userId",
                        GET, null,
                        new ParameterizedTypeReference<FindRoomsResponse[]>() {
                        });
            FindRoomsResponse response = res.getBody()[0];
            assertNotNull(response.getRoomUuid());
            assertEquals("roomName", response.getRoomName());
            assertEquals("lastMessage", response.getLastMessage());
      }

      @Test
      void should_update_room_message() {
            Room savedRoom = roomRepo.save(new Room("roomName", "lastMessage", "img_url"));
            roomRepo.saveAndFlush(savedRoom);

            SaveMessageRequest saveMessageRequest= new SaveMessageRequest("newMessage","a@g.com",savedRoom.getUuid());
            restTemplate.put("/api/v1/rooms/", saveMessageRequest);
      }
}
