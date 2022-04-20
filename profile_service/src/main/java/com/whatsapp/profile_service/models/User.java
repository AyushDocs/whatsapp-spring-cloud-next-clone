package com.whatsapp.profile_service.models;

import javax.persistence.Entity;

import com.whatsapp.library_service.BaseEntity;

import lombok.Data;
@Data
@Entity
public class User extends BaseEntity {
      private String email;
      private String password;
      private String username;
      private String roles;
}