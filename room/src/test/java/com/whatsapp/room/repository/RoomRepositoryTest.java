package com.whatsapp.room.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.models.Room;
import com.whatsapp.room.models.RoomUserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
@DataJpaTest
public class RoomRepositoryTest {
      @Autowired
      private RoomRepository roomRepository;
      @Autowired
      private TestEntityManager em;
      @BeforeEach
      void setup() {
            em.clear();
      }
      @Test
      void should_not_fetch_all_rooms_no_userId_room_id_row() {
            Room room=new Room("name","lastMessage","imgUrl");
            em.persist(room);

            FindRoomsResponse[] response = roomRepository.findRoomsWithUnreadMessagesByUserUuid("userUuid");

            assertNull(response);
      }
      @Test
      void should_fetch_all_rooms() {
            Room room=new Room("name","lastMessage","imgUrl");
            em.persistAndFlush(room);
            RoomUserId rui=new RoomUserId(null, room.getUuid(),"userUuid");
            em.persistAndFlush(rui);

            FindRoomsResponse[] response = roomRepository.findRoomsWithUnreadMessagesByUserUuid("userUuid");

            assertNotNull(response);
      }
}
