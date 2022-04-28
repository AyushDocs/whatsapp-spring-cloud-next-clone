package com.whatsapp.messages.repositories;

import com.whatsapp.messages.models.MessageUserId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MessageUserIdRepository extends JpaRepository<MessageUserId,Long>{

}
