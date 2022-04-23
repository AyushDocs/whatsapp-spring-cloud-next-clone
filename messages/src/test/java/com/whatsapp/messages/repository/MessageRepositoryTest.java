package com.whatsapp.messages.repository;

import static com.whatsapp.messages.models.MessageStatus.RECEIVED_BY_SERVER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.models.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class MessageRepositoryTest {
      @Autowired
      private MessageRepository messageRepository;
      @Autowired
      private TestEntityManager em;

      @BeforeEach
      void setup() {
            em.clear();
      }

      @Test
      void should_not_fetch_all_rooms_no_userId_room_id_row() {
            Message message = new Message("content",
                        "userUuid",
                        RECEIVED_BY_SERVER,
                        "roomId");
            em.persist(message);

            FindMessageResponse[] response = messageRepository.findUnreadMessages("roomId", "userUuid");

            assertNotNull(response);
      }
}
