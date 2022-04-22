package com.whatsapp.room.models;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserId {
      @Id
      @GeneratedValue(strategy=SEQUENCE)
      private Long id;
      private String roomUuid;
      private String userUuid;
}
