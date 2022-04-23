package com.whatsapp.images.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import com.whatsapp.images.configuration.ImageConfig;
import com.whatsapp.images.configuration.ImageTypes;
import com.whatsapp.images.lib.ImageOperations;
import com.whatsapp.images.models.UserImage;
import com.whatsapp.images.repositories.ImageRepo;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
      private ImageService underTest;
      @Mock
      private ImageRepo imageRepo;
      @Mock
      private ImageConfig imageConfig;
      @Mock
      private ImageOperations imageOperations;

      @Before
      public void setUp() {
            underTest = new ImageService(imageRepo, imageConfig, imageOperations);
      }

      @Test
      public void should() throws Exception {
            when(imageConfig.getImageFormats())
                        .thenReturn(new String[] { "jpg", "jpeg", "png", "gif", "bmp", "webp" });
            when(imageConfig.getImageUploadDir())
                        .thenReturn("user-photos");
            BufferedImage image = new BufferedImage(500, 500, 1);
            when(imageOperations.read(any(InputStream.class))).thenReturn(image);
            byte[] imageBytes = "image".getBytes();

            underTest.storeProfileImage(imageBytes, "userUuid");

            ArgumentCaptor<UserImage> ac = ArgumentCaptor.forClass(UserImage.class);
            verify(imageRepo).save(ac.capture());
            verify(imageOperations).read(any(InputStream.class));
            verify(imageOperations, times(6))
                        .write(any(BufferedImage.class), anyString(), any(File.class));
            UserImage userImage = ac.getValue();
            assertEquals(ImageTypes.PROFILE, userImage.getImageType());
            assertEquals("userUuid", userImage.getUserUuid());
      }
}
