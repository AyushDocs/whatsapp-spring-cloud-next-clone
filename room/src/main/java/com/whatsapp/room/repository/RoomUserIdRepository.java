package com.whatsapp.room.repository;

import java.util.List;

import com.whatsapp.room.models.RoomUserId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomUserIdRepository extends JpaRepository<RoomUserId, Long> {
      @Query("SELECT rui.userUuid FROM RoomUserId rui WHERE roomUuid = ?1")
      List<String> findUserUuidByRoomUuid(String roomUuid);
      
}