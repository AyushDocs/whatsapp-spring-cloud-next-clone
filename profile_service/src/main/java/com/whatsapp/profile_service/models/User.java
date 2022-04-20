package com.whatsapp.profile_service.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE)
      private Long id;
      private String uuid;
      private String email;
      private String password;
      private String username;
      private String roles;
      
      @PrePersist
      public void prePersist() {
            this.uuid = UUID.randomUUID().toString();
      }
}
