package com.whatsapp.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whatsapp.library.Response;
import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.dto.SaveMessageRequest;
import com.whatsapp.room.dto.SaveRoomRequest;
import com.whatsapp.room.dto.SaveRoomResponse;
import com.whatsapp.room.service.MessageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureJson
class IntegrationTest {
      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired
      private EntityManager em;
      @MockBean
      private MessageService messageService;

      @BeforeEach
      void setup() {
            em.clear();
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void should_not_save_room(String text) throws JsonProcessingException, Exception {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        text,
                        text,
                        Arrays.asList(text));

            ResponseEntity<Response<Map<String, String>>> res = restTemplate
                        .exchange("/api/v1/rooms/", POST, new HttpEntity<SaveRoomRequest>(saveRoomRequest),
                                    new ParameterizedTypeReference<Response<Map<String, String>>>() {

                                    });

            assertEquals(UNPROCESSABLE_ENTITY, res.getStatusCode());
            assertNotNull(res.getBody());
            Map<String, String> data = res.getBody().getData();
            assertNotNull(data);
            assertEquals(UNPROCESSABLE_ENTITY, res.getStatusCode());
            assertNotNull(data.get("name"));
            assertNotNull(data.get("imgUrl"));
            assertNotNull(data.get("users"));
      }

      @Test
      void should_save_room() throws JsonProcessingException, Exception {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        "name",
                        "http://localhost:8080/api/v1/images",
                        List.of("ayushUuid", "heUuid"));

            ResponseEntity<Response<Map<String, String>>> res = restTemplate
                        .exchange("/api/v1/rooms/", POST, new HttpEntity<SaveRoomRequest>(saveRoomRequest),
                                    new ParameterizedTypeReference<Response<Map<String, String>>>() {

                                    });

            assertEquals(201, res.getStatusCodeValue());
      }

      @Test
      void should_find_rooms_with_unread_messages() throws Exception {
            String uuid = createRoomAndGetItsUuid();
            addMessage(uuid);
            // unread room starts

            ResponseEntity<FindRoomsResponse[]> res = restTemplate
                        .getForEntity("/api/v1/rooms/userUuid", FindRoomsResponse[].class);

            assertEquals(200, res.getStatusCodeValue());
            assertNotNull(res.getBody());
            assertEquals(1, res.getBody().length);
            assertEquals("http://localhost:8080/api/v1/images", res.getBody()[0].getImgUrl());
            assertEquals("lastMessage", res.getBody()[0].getLastMessage());
      }

      private void addMessage(String uuid) {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest("lastMessage", "sentBy", uuid);
            restTemplate.exchange("/api/v1/rooms/", HttpMethod.PUT,
                        new HttpEntity<SaveMessageRequest>(saveMessageRequest),
                        Void.class);
      }

      private String createRoomAndGetItsUuid() {
            SaveRoomRequest saveRoomRequest = new SaveRoomRequest(
                        "roomName",
                        "http://localhost:8080/api/v1/images",
                        List.of("ayushUuid", "userUuid"));

            return restTemplate
                        .exchange("/api/v1/rooms/", POST, new HttpEntity<SaveRoomRequest>(saveRoomRequest),
                                    new ParameterizedTypeReference<Response<SaveRoomResponse>>() {

                                    })
                        .getBody().getData().getUuid();
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void should_not_update_room_last_message(String text) throws JsonProcessingException, Exception {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest(text, text, text);
            ResponseEntity<Response<Map<String, String>>> res = restTemplate.exchange("/api/v1/rooms/", HttpMethod.PUT,
                        new HttpEntity<SaveMessageRequest>(saveMessageRequest),
                        new ParameterizedTypeReference<Response<Map<String, String>>>() {

                        });
            assertEquals(UNPROCESSABLE_ENTITY, res.getStatusCode());
            assertNotNull(res.getBody());
            Map<String, String> data = res.getBody().getData();
            assertNotNull(data);
            assertNotNull(data.get("roomUuid"));
            assertNotNull(data.get("content"));
            assertNotNull(data.get("sentBy"));
      }

      @Test
      void should_update_room_last_message() throws Exception {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest("content", "sentBy", "roomUuid");

            ResponseEntity<Response<Map<String, String>>> res = restTemplate.exchange("/api/v1/rooms/", HttpMethod.PUT,
                        new HttpEntity<SaveMessageRequest>(saveMessageRequest),
                        new ParameterizedTypeReference<Response<Map<String, String>>>() {

                        });

            assertEquals(OK, res.getStatusCode());
      }
}