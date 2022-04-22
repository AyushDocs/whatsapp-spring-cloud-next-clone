package com.whatsapp.messages.models;

import static javax.persistence.EnumType.ORDINAL;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

import com.whatsapp.library.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
      private String content;
      private String sentBy;
      @Enumerated(ORDINAL)
      private Status status;
      private String roomUuid;

      public enum Status{
            RECEIVED_BY_SERVER,
            SENT_BY_SERVER,
            SEEN_BY_CLIENT2,
      }
}