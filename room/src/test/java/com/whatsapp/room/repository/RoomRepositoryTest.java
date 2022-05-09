package com.whatsapp.room.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

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
      private RoomRepository underTest;
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

            List<FindRoomsResponse> response = underTest.findRoomsWithUnreadMessagesByUserUuid("userUuid");

            assertNull(response);
      }
      @Test
      void should_fetch_all_rooms() {
            Room room=new Room("name","lastMessage","imgUrl");
            em.persistAndFlush(room);
            RoomUserId rui=new RoomUserId(null, room.getUuid(),"userUuid");
            em.persistAndFlush(rui);

            List<FindRoomsResponse> response = underTest.findRoomsWithUnreadMessagesByUserUuid("userUuid");

            assertNotNull(response);
      }
      @Test
      void should_update_room_last_message() {
            Room room=new Room("name","lastMessage","imgUrl");
            Room savedRoom = em.persistAndFlush(room);

            String uuid = savedRoom.getUuid();
            System.out.println(room.getUuid());
            System.out.println(savedRoom.getUuid());

            underTest.saveMessage(uuid,"content");

            Room roomFound = em.find(Room.class,savedRoom.getId());
            assertEquals("content",roomFound.getLastMessage());
      }
}
