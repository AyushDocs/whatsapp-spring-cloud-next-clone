package com.whatsapp.images.repositories;

import com.whatsapp.images.models.UserImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ImageRepo extends JpaRepository<UserImage, Long> {

}
