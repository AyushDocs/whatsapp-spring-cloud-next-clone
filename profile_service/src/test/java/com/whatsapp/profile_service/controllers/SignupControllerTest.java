package com.whatsapp.profile_service.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.config.JwtConfig;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.exceptions.InvalidCredentialsException;
import com.whatsapp.profile_service.services.AuthService;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SignupController.class)
@ExtendWith(MockitoExtension.class)
class SignupControllerTest {
      @MockBean
      private JwtConfig jwtConfig;
      @MockBean
      private AuthService authService;
      @MockBean
      private JwtUtils jwtUtils;
      @Autowired
      private MockMvc mvc;
      @Autowired
      private SignupController signupController;
      private static final ObjectMapper mapper = new ObjectMapper();
      private SignupRequest signupRequestPayload;
      private MockHttpServletRequestBuilder mockSignupRequest;

      @BeforeEach
       void setUp() {
            signupRequestPayload = SignupRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .username("username")
                        .build();

            mockSignupRequest = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequestPayload))
                        .contentType(MediaType.APPLICATION_JSON);
            when(jwtUtils.verifyToken(anyString())).thenReturn(false);
            when(jwtConfig.getCookieName()).thenReturn("token");

      }

      @Test
       void signup_should_fail_no_body() throws Exception {
            // arrange
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON);
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
       void signup_should_fail_content_type_not_set() throws Exception {
            // arrange
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .contentType(MediaType.TEXT_HTML);
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
      }

      @ParameterizedTest()
      @NullAndEmptySource
      @ValueSource(strings = { "ayushgmail.com", " " })
       void signup_should_fail_improper_email(String email) throws Exception {
            // arrange
            signupRequestPayload.setEmail(email);
            MockHttpServletRequestBuilder request = mockSignupRequest
                  .content(asJsonString(signupRequestPayload));
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest()
      @NullAndEmptySource
      @ValueSource(strings = { " " })
       void signup_should_fail_username(String username) throws Exception {
            // arrange
            signupRequestPayload.setUsername(username);
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
       void signup_should_fail_password(String password) throws Exception {
            // arrange
            signupRequestPayload.setPassword(password);
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
       void signup_should_fail_email_and_password(String value) throws Exception {
            // arrange
            signupRequestPayload.setPassword(value);
            signupRequestPayload.setEmail(value);
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
       void signup_should_not_succeed_user_already_exists() throws Exception {
            // arrange
            doThrow(new InvalidCredentialsException())
                        .when(authService).signup(signupRequestPayload.toUser());
            // act
            ResultActions result = mvc.perform(mockSignupRequest);
            // assert
            result.andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
       void signup_should_succeed() throws Exception {
            //act
            ResultActions result = mvc.perform(mockSignupRequest);
            //assert
            result.andExpect(MockMvcResultMatchers.status().isCreated());
            verify(authService).signup(signupRequestPayload.toUser());
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
