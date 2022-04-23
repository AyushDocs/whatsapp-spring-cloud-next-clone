package com.whatsapp.images.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
public class ImageConfig {
   @Value("${image.upload.dir}")
   private String imageUploadDir;
   private final String[] imageFormats = { "jpg", "jpeg", "png", "gif", "bmp","webp" };
}