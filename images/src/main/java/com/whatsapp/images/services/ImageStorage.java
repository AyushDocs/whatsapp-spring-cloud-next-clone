package com.whatsapp.images.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.whatsapp.images.configuration.ImageConfig;
import com.whatsapp.images.configuration.ImageTypes;
import com.whatsapp.images.lib.ImageOperations;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ImageStorage {
      private final byte[] imageBytes;
      private final String userId;
      private final ImageTypes imageType;
      private final ImageConfig imageConfig;
      private final ImageOperations imageOperations;
      private BufferedImage image;

      public void storeImage() throws IOException {
            createUserDirIfDoesNotExist(userId);
            this.image = getBufferedImage();
            for (String format : imageConfig.getImageFormats())
                  saveFileIn(format);
      }

      private void createUserDirIfDoesNotExist(String userId) throws IOException {
            Path userImagesDirPath = getUserImagesDirPath(userId);// user-images/5
            if (userImagesDirDoesNotExist(userImagesDirPath))
                  createUserDir(userImagesDirPath);
      }

      private void createUserDir(Path uploadPath) throws IOException {
            Files.createDirectories(uploadPath);
      }

      private boolean userImagesDirDoesNotExist(Path uploadPath) {
            return !Files.exists(uploadPath);
      }

      private void saveFileIn(String format) throws IOException {
            String fullPathToFile = "%s/%s.%s".formatted(getUserImagesDirPath(userId), imageType.toString(), format);
            File file = getFileFromPath(fullPathToFile);
            imageOperations.write(this.image, format, file);
      }

      private File getFileFromPath(String fullPathToFile) {
            return Paths.get(fullPathToFile).toFile();
      }

      private BufferedImage getBufferedImage() throws IOException {
            return imageOperations.read(new ByteArrayInputStream(imageBytes));
      }

      private Path getUserImagesDirPath(String userId) {
            String userImagesDirPath = "%s/%s"
                        .formatted(imageConfig.getImageUploadDir(), userId);
            return Paths.get(userImagesDirPath);
      }
}
