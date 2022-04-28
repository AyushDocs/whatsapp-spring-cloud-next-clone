package com.whatsapp.messages.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.models.Message;
import com.whatsapp.messages.models.MessageUserId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class MessageRepositoryTest {
      @Autowired
      private MessageRepository underTest;
      @Autowired
      private TestEntityManager em;
      
      private static final String SENT_BY = "userEmail";
      private static final String ROOM_UUID = "roomUuid";
      private static final String CONTENT = "content";
      private static final String FINDER_EMAIL = "a@g.com";

      @Test
      void should_save_message_into_db() {
            Message m = new Message(CONTENT, ROOM_UUID, SENT_BY);
            em.persistAndFlush(m);
            MessageUserId mui = new MessageUserId(null, FINDER_EMAIL, m.getUuid());
            em.persistAndFlush(mui);

            List<ResponseMessage> res = underTest
                        .findUnreadMessages(FINDER_EMAIL, ROOM_UUID);

            ResponseMessage firstMessage = res.get(0);
            assertEquals(CONTENT, firstMessage.getContent());
            assertEquals(SENT_BY, firstMessage.getSentByEmail());
      }
}