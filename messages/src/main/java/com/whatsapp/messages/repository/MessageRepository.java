package com.whatsapp.messages.repository;

import com.whatsapp.messages.dto.FindMessageResponse;
import com.whatsapp.messages.models.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
      @Query("SELECT new com.whatsapp.messages.dto.FindMessageResponse(m.content,m.sentBy,m.createdAt) "
                  + "FROM Message m "
                  + "WHERE m.roomUuid=?1 "
                  + "AND m.sentBy=?2 "
                  + "AND m.status=com.whatsapp.messages.models.MessageStatus.RECEIVED_BY_SERVER")
      FindMessageResponse[] findUnreadMessages(String roomId, String userUuid);
}