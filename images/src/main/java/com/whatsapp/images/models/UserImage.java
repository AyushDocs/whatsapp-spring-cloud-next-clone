package com.whatsapp.images.models;

import static javax.persistence.EnumType.ORDINAL;
import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.whatsapp.images.configuration.ImageTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "image_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class UserImage {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    private String userUuid;
    @Enumerated(ORDINAL)
    private ImageTypes imageType;
}
