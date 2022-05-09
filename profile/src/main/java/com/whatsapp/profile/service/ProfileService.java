package com.whatsapp.profile.service;

import javax.validation.Valid;

import com.whatsapp.profile.dto.AddImageUrlDto;
import com.whatsapp.profile.dto.FindUserDto;
import com.whatsapp.profile.dto.SaveUserDto;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.repository.ProfileRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
      private final ProfileRepository profileRepository;

      public Page<UserResponseDto> findUsersByText(String text, int offset, int limit) {
            return profileRepository.findPossibleFriends(text, PageRequest.of(offset, limit));
      }

      public void saveUser(SaveUserDto saveUserDto) {
            profileRepository.save(saveUserDto.toProfile());
      }

      public FindUserDto findByEmail(String email) {
            return profileRepository.findByEmail(email);
      }

      public void updateImageUrl(AddImageUrlDto addImageUrlDto) {
            profileRepository.updateImageUrlByUserUuid(addImageUrlDto.getImageUrl(),addImageUrlDto.getUserUuid());
      }

}