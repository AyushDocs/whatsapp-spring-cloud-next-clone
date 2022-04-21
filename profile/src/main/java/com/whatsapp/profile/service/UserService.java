package com.whatsapp.profile.service;

import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
      private final UserRepository userRepository;

      public Page<UserResponseDto> findUsersByText(String text, int offset, int limit) {
            return userRepository.findPossibleFriends(text, PageRequest.of(offset, limit));
      }

}