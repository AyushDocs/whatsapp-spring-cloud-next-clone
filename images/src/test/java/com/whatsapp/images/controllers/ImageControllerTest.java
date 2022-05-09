package com.whatsapp.images.controllers;

import static com.whatsapp.images.configuration.ImageTypes.PROFILE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.whatsapp.images.services.ImageService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ImageController.class)
@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {
      @Autowired
      private ImageController imageController;
      @Autowired
      private MockMvc mvc;
      @MockBean
      private ImageService imageService;

      @Test
      void controller_shoul_be_created() throws Exception {
            assertNotNull(imageController);
      }

      @Test
      void should_upload_image() throws Exception {
            byte[] bytes = "image-file".getBytes();
            ResultActions result = mvc.perform(multipart("/api/v1/images/")
            .file("image",bytes)
            .param("userUuid", "userUuid"));

            verify(imageService).storeProfileImage(bytes, "userUuid");
            result.andExpect(status().isAccepted());
      }

      @Test
      void should_find_images() throws Exception {
            String image="chbuvbyfbcewiyfcbeyfvceyuv";
            when(imageService.findImages(PROFILE, "jpg", "userUuid")).thenReturn(image);
            MockHttpServletRequestBuilder request = get("/api/v1/images")
            .param("userUuid", "userUuid")
            .param("imageType", "profile")
            .param("format", "jpg");

            ResultActions result = mvc.perform(request);

            verify(imageService).findImages(PROFILE, "jpg", "userUuid");
            result.andExpect(status().isOk());
            result.andExpect(MockMvcResultMatchers.content().string(image));
      }
}
