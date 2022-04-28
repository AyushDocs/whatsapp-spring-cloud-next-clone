package com.whatsapp.messages.models;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class MessageUserId {
      @Id
      @GeneratedValue(strategy = IDENTITY)
      private Long id;
      private String userEmail;
      private String messageUuid;
}