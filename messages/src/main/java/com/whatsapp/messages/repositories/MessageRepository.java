package com.whatsapp.messages.repositories;

import java.util.List;

import com.whatsapp.messages.dto.ResponseMessage;
import com.whatsapp.messages.models.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query("SELECT new com.whatsapp.messages.dto.ResponseMessage(m.content,m.createdAt,m.sentByEmail)"
			+ "FROM Message m, MessageUserId mui "
			+ "WHERE mui.userEmail=:userEmail "
			+ "AND m.uuid=mui.messageUuid "
			+ "AND m.roomUuid =:roomUuid")
	List<ResponseMessage> findUnreadMessages(
			@Param(value = "userEmail") String userEmail,
			@Param("roomUuid") String roomUuid);
}
