package com.whatsapp.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.LinkedHashMap;
import java.util.List;

import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.dto.SaveMessageRequest;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.models.MessageUserId;
import com.whatsapp.messages.repositories.MessageRepository;
import com.whatsapp.messages.repositories.MessageUserIdRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class IntegrationTest {
      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired
      private MessageRepository mRepo;
      @Autowired
      private MessageUserIdRepository muiRepo;

      @Test
      void should_send_message() {
            SaveMessageRequest request = new SaveMessageRequest(
                        "content",
                        "sendBy",
                        "roomUuid",
                        List.of("a@g.com", "b@g.com", "c@g.com"));
            ResponseEntity<Void> res = restTemplate.postForEntity("/api/v1/messages",
                        request,
                        Void.class);
            assertEquals(201, res.getStatusCodeValue());
      }

      @Test
      void should_find_message() {
            Message m = new Message("content",
                        "roomUuid",
                        "sentBy");
            mRepo.saveAndFlush(m);
            assertNotNull(m.getUuid());
            MessageUserId mui = new MessageUserId(null, "a@g.com", m.getUuid());
            muiRepo.save(mui);

            ResponseEntity<List> res = restTemplate
                        .getForEntity("/api/v1/messages?email=a@g.com&roomUuid=roomUuid",
                                    List.class);
            assertEquals(206, res.getStatusCodeValue());
            List<LinkedHashMap> typedRes=(List<LinkedHashMap>)res.getBody();
            // assertEquals("",typedRes.toString());
            LinkedHashMap firstMessage = typedRes.get(0);
            assertEquals("content",firstMessage.get("content"));
            assertEquals("sentBy",firstMessage.get("sentByEmail"));
      }
}
