package com.whatsapp.room.repository;

import com.whatsapp.room.models.RoomUserId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomUserIdRepository extends JpaRepository<RoomUserId, Long> {
}