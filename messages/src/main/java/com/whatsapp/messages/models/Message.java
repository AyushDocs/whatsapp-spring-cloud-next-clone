package com.whatsapp.messages.models;

import javax.persistence.Entity;

import com.whatsapp.library.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Message extends BaseEntity {
      private String content;
      private String roomUuid;
      private String sentByEmail;
}
