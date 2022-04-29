package com.whatsapp.profile.models;

import javax.persistence.Entity;

import com.whatsapp.library.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseEntity {
      private String email;
      private String username;
      private String imgUrl;
}