package com.whatsapp.library;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.*;
import java.util.*;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity  {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE)
      @JsonIgnore
      private Long id;
      private String uuid;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;

      @PrePersist
      void init() {
            this.uuid =UUID.randomUUID().toString();
      }

      public Long getId() {
            return this.id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getUuid() {
            return this.uuid;
      }

      public void setUuid(String uuid) {
            this.uuid = uuid;
      }

      public LocalDateTime getCreatedAt() {
            return this.createdAt;
      }

      public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
      }

      public LocalDateTime getUpdatedAt() {
            return this.updatedAt;
      }

      public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
      }

}
