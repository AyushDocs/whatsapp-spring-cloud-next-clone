package com.whatsapp.room.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.whatsapp.room.dto.FindRoomsResponse;
import com.whatsapp.room.models.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
      List<FindRoomsResponse> findRoomsWithUnreadMessagesByUserUuid(String userUuid);
      
      
      @Modifying(flushAutomatically = true)
      @Transactional
      @Query("UPDATE Room r SET r.lastMessage = ?2 WHERE r.uuid = ?1")
      void saveMessage(String roomUuid, String content);
}