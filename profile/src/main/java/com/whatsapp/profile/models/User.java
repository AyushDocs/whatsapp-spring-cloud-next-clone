package com.whatsapp.profile.models;

import javax.persistence.Entity;

import com.whatsapp.library.BaseEntity;

import lombok.Data;
@Data
@Entity
public class User extends BaseEntity {
      private String email;
      private String username;
      private String imgUrl;
}