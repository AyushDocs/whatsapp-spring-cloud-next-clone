package com.whatsapp.profile.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.whatsapp.library.Response;
import com.whatsapp.profile.dto.SaveUserDto;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.exceptions.InvalidInputException;
import com.whatsapp.profile.service.ProfileService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@WebMvcTest(ProfileController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
      @Autowired
      private ProfileController userController;
      @MockBean
      private ProfileService userService;

      private static final String UUID = "uuid";
      private static final String USERNAME = "Ayush";
      private static final String EMAIL = "a@g.com";
      private static final String IMG_URL = "http://localhost:8082/api/v1/images?email=" + EMAIL;
      private static final String TEXT = "ayu";

      @ParameterizedTest
      @EmptySource
      @ValueSource(strings = { " " })
      void should_not_fetch_all_users(String text) {
            // act
            Executable action=()->userController.findPossibleFriends(text, 0, 2);
            // assert
            assertThrows(InvalidInputException.class, action);
            verify(userService, times(0)).findUsersByText(anyString(), anyInt(), anyInt());
      }

      @Test
      void should_fetch_all_users(){
            // arrange
            UserResponseDto responseDto = new UserResponseDto(EMAIL, IMG_URL, USERNAME, UUID);
            Page<UserResponseDto> users = new PageImpl<>(List.of(responseDto));
            when(userService.findUsersByText(anyString(), anyInt(), anyInt())).thenReturn(users);
            // act
            Response<Page<UserResponseDto>> result = userController.findPossibleFriends(TEXT, 0, 2);

            // assert
            verify(userService).findUsersByText(anyString(), anyInt(), anyInt());
            Page<UserResponseDto> pageData = result.getData();
            assertNotNull(pageData);
            UserResponseDto userResponseDto = pageData.getContent().get(0);
            assertEquals(EMAIL, userResponseDto.getEmail());
            assertEquals(IMG_URL, userResponseDto.getImgUrl());
            assertEquals(USERNAME, userResponseDto.getUsername());
            assertEquals(UUID, userResponseDto.getUuid());
      }
      @Test
      void should_not_add_users(){
            // arrange
            SaveUserDto saveUserDto = new SaveUserDto("email","username");

            userController.saveUser(saveUserDto);

            assertNotNull(userController.findByEmail("email"));
      }
}