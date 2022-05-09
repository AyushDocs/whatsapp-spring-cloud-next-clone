package com.whatsapp.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.authentication.config.JwtConfig;
import com.whatsapp.authentication.dto.LoginRequest;
import com.whatsapp.authentication.exceptions.InvalidCredentialsException;
import com.whatsapp.authentication.services.LoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LoginController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureJson
class LoginControllerTest {
      @MockBean
      private LoginService loginService;
      @MockBean
      private JwtConfig jwtConfig;
      @Autowired
      private MockMvc mvc;
      @Autowired
      private LoginController loginController;

      private LoginRequest loginRequestPayload;
      private MockHttpServletRequestBuilder mockLoginRequest;
      @Autowired
      private ObjectMapper mapper;
      private static final String PASSWORD = "secret";
      private static final String EMAIL = "ayush@gmail.com";

      @BeforeEach
      void init() {
            loginRequestPayload = LoginRequest.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            mockLoginRequest = MockMvcRequestBuilders.post("/api/v1/authentication/login")
                        .content(asJsonString(loginRequestPayload))
                        .contentType(MediaType.APPLICATION_JSON);
      }

      @Test
      void controllerIsPresent() throws Exception {
           assertNotNull(loginController);
      }
      @Test
      void login_should_fail_no_body() throws Exception {
            // act
            MockHttpServletRequestBuilder request = mockLoginRequest
                        .content("");
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isBadRequest());
      }

      @Test
      void login_should_fail_no_content_type_set() throws Exception {
            // act
            MockHttpServletRequestBuilder request = mockLoginRequest
                        .contentType(MediaType.TEXT_HTML);
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isUnsupportedMediaType());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { "ayushgmail.com", " " })
      void login_should_fail_improper_email(String email) throws Exception {
            // act
            loginRequestPayload.setEmail(email);
            MockHttpServletRequestBuilder request = mockLoginRequest
                        .content(asJsonString(loginRequestPayload));
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void login_should_fail_no_password(String password) throws Exception {
            // act
            loginRequestPayload.setPassword(password);
            MockHttpServletRequestBuilder request = mockLoginRequest.content(asJsonString(
                        loginRequestPayload));
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isBadRequest());
      }

      @Test
      void login_should_fail_no_email_and_password() throws Exception {
            // act
            loginRequestPayload.setEmail(null);
            loginRequestPayload.setPassword(null);
            MockHttpServletRequestBuilder request = mockLoginRequest.content(asJsonString(
                        loginRequestPayload));
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isBadRequest());
      }

      @Test
      void login_should_not_succeed_invalid_user() throws Exception {
            // arrange
            when(loginService.login(EMAIL, PASSWORD)).thenThrow(new InvalidCredentialsException());
            // act
            ResultActions result = mvc.perform(mockLoginRequest);
            // assert
            result.andExpect(status().isBadRequest());
            verify(loginService).login(EMAIL, PASSWORD);
      }

      @Test
      void login_should_succeed() throws Exception {
            // arrange
            when(jwtConfig.getCookieName()).thenReturn("token");
            when(loginService.login(EMAIL, PASSWORD)).thenReturn("jwt");
            // act
            ResultActions result = mvc.perform(mockLoginRequest);
            // assert
            verify(loginService).login(EMAIL, PASSWORD);
            String cookieName = "token";
            result.andExpect(status().isOk());
            result.andExpect(MockMvcResultMatchers.cookie().exists(cookieName));
            result.andExpect(MockMvcResultMatchers.cookie().httpOnly(cookieName, true));
            result.andExpect(MockMvcResultMatchers.cookie().value(cookieName, "jwt"));
            result.andExpect(MockMvcResultMatchers.cookie().secure(cookieName, true));
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