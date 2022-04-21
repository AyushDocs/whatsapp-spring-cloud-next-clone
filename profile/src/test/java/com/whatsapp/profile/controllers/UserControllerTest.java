package com.whatsapp.profile.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
      @Autowired
      private MockMvc mvc;
      @Autowired
      private UserController userController;
      
      @MockBean
      private UserService userService;

      private static final String UUID = "uuid";
      private static final String USERNAME = "Ayush";
      private static final String EMAIL = "a@g.com";
      private static final String IMG_URL = "http://localhost:8082/api/v1/images?email=" + EMAIL;
      private static final String TEXT = "ayu";

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void should_not_fetch_all_users(String text) throws Exception {
            // arrange
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .get("/api/v1/users/")
                        .param("text", text);
            // act
            ResultActions result = mvc.perform(request);

            // assert
            verify(userService, times(0)).findUsersByText(anyString(), anyInt(), anyInt());
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void should_fetch_all_users() throws Exception {
            // arrange
            UserResponseDto responseDto = new UserResponseDto(EMAIL, IMG_URL, USERNAME, UUID);
            Page<UserResponseDto> users = new PageImpl<>(List.of(responseDto));
            when(userService.findUsersByText(anyString(), anyInt(), anyInt())).thenReturn(users);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .get("/api/v1/users/")
                        .param("text", TEXT);
            // act
            ResultActions result = mvc.perform(request);

            // assert
            verify(userService).findUsersByText(anyString(), anyInt(), anyInt());
            result.andExpect(MockMvcResultMatchers.status().isPartialContent());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].email").value(EMAIL));
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].password").doesNotExist());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].img_url").value(IMG_URL));
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].username").value(USERNAME));
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].roles").doesNotExist());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").doesNotExist());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].uuid").value(UUID));
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].createdAt").doesNotExist());
            result.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].updatedAt").doesNotExist());
      }
}