package com.whatsapp.images.services;

import static com.whatsapp.images.configuration.ImageTypes.PROFILE;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import com.whatsapp.images.configuration.ImageConfig;
import com.whatsapp.images.configuration.ImageTypes;
import com.whatsapp.images.exceptions.ImageNotFoundException;
import com.whatsapp.images.lib.ImageOperations;
import com.whatsapp.images.models.UserImage;
import com.whatsapp.images.repositories.ImageRepo;

import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageConfig imageConfig;
    private final ImageOperations imageOperations;
    private static final String IMAGE_STORAGE_ERROR_MESSAGE_FORMAT = "Error while storing profile image for userId {}";

    @Async
    public void storeProfileImage(byte[] imageBytes, String userUuid) {
        ImageStorage imageStorer = new ImageStorage(imageBytes,
                userUuid,
                PROFILE,
                imageConfig,
                imageOperations);
        try {
            imageStorer.storeImage();
        } catch (IOException e) {
            log.error(IMAGE_STORAGE_ERROR_MESSAGE_FORMAT, userUuid, e);
        }
        saveImageInDb(userUuid, ImageTypes.PROFILE);
    }

    public String findImages(ImageTypes imageType, String format, String userUuid) {
        String imageFilePath = getImageFilePath(imageType, format, userUuid);
        File imageFile = new File(imageFilePath);
        try {
            return getBase64ImageLink(format, imageFile);
        } catch (IOException e) {
            log.error("could not find image with imageType {}, format {} and userId {}", imageType.name(), format,
                    userUuid);
            throw new ImageNotFoundException();
        }
    }

    private String getBase64ImageLink(String format, File imageFile) throws IOException {
        byte[] file = readFile(imageFile);
        String encodedString = base64EncodeFile(file);
        return "data:image/%s;base64,%s".formatted(format, encodedString);
    }

    private byte[] readFile(File imageFile) throws IOException {
        return FileUtils.readFileToByteArray(imageFile);
    }

    private String base64EncodeFile(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private String getImageFilePath(ImageTypes imageType, String format, String userUuid) {
        return "%s/%s/%s.%s".formatted(imageConfig.getImageUploadDir(), userUuid, imageType.name(), format);
    }

    private void saveImageInDb(String userUuid, ImageTypes imageType) {
        UserImage image = new UserImage(null, userUuid, imageType);
        imageRepo.save(image);
    }
}
