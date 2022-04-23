package com.whatsapp.images.controllers;

import static com.whatsapp.images.configuration.ImageTypes.PROFILE;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;

import com.whatsapp.images.services.ImageService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(ImageController.class)
@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {
      @Autowired
      private ImageController imageController;
      @MockBean
      private ImageService imageService;
      
      @Test
      void should_upload_image() throws Exception{
            byte[] bytes="image-file".getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            MultipartFile image=new MockMultipartFile("profile",bais);

            imageController.uploadImage(image, "userUuid");

            verify(imageService).storeProfileImage(bytes, "userUuid");
      }
      @Test
      void should_find_images() throws Exception{
            imageController.findImage("userUuid", PROFILE, "format");

            verify(imageService).findImages(PROFILE, "format", "userUuid");
      }
}
