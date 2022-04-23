package com.whatsapp.images.controllers;

import java.io.IOException;

import com.whatsapp.images.configuration.ImageTypes;
import com.whatsapp.images.services.ImageService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam MultipartFile image, @RequestParam String userUuid) throws IOException{
        byte[] imageBytes = image.getBytes();
        imageService.storeProfileImage(imageBytes, userUuid);
    }

    @GetMapping
    public String findImage(@PathVariable String userUuid,
            @PathVariable ImageTypes imageType,
            @PathVariable String format) {
        return imageService.findImages(imageType,format,userUuid);
    }
}
