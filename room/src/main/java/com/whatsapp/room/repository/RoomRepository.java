package com.whatsapp.room.repository;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.models.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
      @Query("SELECT new com.whatsapp.room.dto.FindRoomsResponse " +
                  "(r.uuid,r.name,r.lastMessage,r.updatedAt) " +
                  "FROM Room r " +
                  "WHERE r.uuid IN " +
                  "(SELECT rui.roomUuid FROM RoomUserId rui " +
                  "WHERE rui.userUuid = ?1)")
      FindRoomsResponse[] findRoomsWithUnreadMessagesByUserUuid(String userUuid);
}