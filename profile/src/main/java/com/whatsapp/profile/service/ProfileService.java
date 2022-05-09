package com.whatsapp.profile.service;

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
      private final ProfileRepository userRepository;

      public Page<UserResponseDto> findUsersByText(String text, int offset, int limit) {
            return userRepository.findPossibleFriends(text, PageRequest.of(offset, limit));
      }

      public void saveUser(SaveUserDto saveUserDto) {
            userRepository.save(saveUserDto.toProfile());
      }

      public FindUserDto findByEmail(String email) {
            return userRepository.findByEmail(email);
      }

}