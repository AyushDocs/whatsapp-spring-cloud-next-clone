package com.whatsapp.profile_service.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.config.JwtConfig;
import com.whatsapp.profile_service.controllers.AuthController;
import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.exceptions.InvalidCredentialsException;
import com.whatsapp.profile_service.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@WebMvcTest(AuthController.class)
@RunWith(SpringRunner.class)
class LoginControllerTest {
      @Autowired
      private MockMvc mvc;
      @MockBean
      private AuthService userService;
      @MockBean
      private JwtConfig jwtConfig;
      
      private LoginRequest loginRequestPayload;
      private MockHttpServletRequestBuilder mockLoginRequest;

      private static final ObjectMapper mapper = new ObjectMapper();
      private static final String PASSWORD = "secret";
      private static final String EMAIL = "ayush@gmail.com";

      @BeforeEach
      void setUp() {
            loginRequestPayload = LoginRequest
                        .builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            mockLoginRequest = MockMvcRequestBuilders.post("/api/v1/users/login")
                        .content(asJsonString(loginRequestPayload))
                        .contentType(MediaType.APPLICATION_JSON);
      }

      @Test
      void login_should_fail_no_body() throws Exception {
            MockHttpServletRequestBuilder request = mockLoginRequest
                        .content("");
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_content_type_set() throws Exception {
            MockHttpServletRequestBuilder request = mockLoginRequest
                        .contentType(MediaType.TEXT_HTML);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { "ayushgmail.com", " " })
      void login_should_fail_improper_email(String email) throws Exception {
            loginRequestPayload.setEmail(email);
            MockHttpServletRequestBuilder request = mockLoginRequest
                  .content(asJsonString(loginRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void login_should_fail_no_password(String password) throws Exception {
            loginRequestPayload.setPassword(password);
            MockHttpServletRequestBuilder request = mockLoginRequest.content(asJsonString(
                        loginRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_email_and_password() throws Exception {
            loginRequestPayload.setEmail(null);
            loginRequestPayload.setPassword(null);
            MockHttpServletRequestBuilder request = mockLoginRequest.content(asJsonString(
                        loginRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_not_succeed_invalid_user() throws Exception {
            when(userService.login(EMAIL, PASSWORD)).thenThrow(new InvalidCredentialsException());
            mvc.perform(mockLoginRequest)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
            verify(userService).login(EMAIL, PASSWORD);
      }

      @Test
      void login_should_succeed() throws Exception {
            when(jwtConfig.getCookieName()).thenReturn("token");
            when(userService.login(EMAIL, PASSWORD)).thenReturn("jwt");
            ResultActions actions = mvc.perform(mockLoginRequest);
            verify(userService).login(EMAIL, PASSWORD);
            String cookieName = "token";
            actions
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.cookie().exists(cookieName))
                        .andExpect(MockMvcResultMatchers.cookie().httpOnly(cookieName, true))
                        .andExpect(MockMvcResultMatchers.cookie().value(cookieName, "jwt"))
                        .andExpect(MockMvcResultMatchers.cookie().secure(cookieName, true));
      }

      private String asJsonString(Object obj) {
            try {
                  return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                  e.printStackTrace();
                  return null;
            }
      }
}
