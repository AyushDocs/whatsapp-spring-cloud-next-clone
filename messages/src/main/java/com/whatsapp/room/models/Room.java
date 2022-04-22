package com.whatsapp.room.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.whatsapp.library.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {
      private String name;
      private String lastMessage;
      private String imgUrl;
}