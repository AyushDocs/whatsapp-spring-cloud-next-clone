package com.whatsapp.profile_service.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.controllers.AuthController;
import com.whatsapp.profile_service.dto.SignupRequest;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@RunWith(SpringRunner.class)
class SignupControllerTest {
      @Autowired
      private MockMvc mvc;
      private static final ObjectMapper mapper = new ObjectMapper();
      private SignupRequest signupRequestPayload;
      @MockBean
      private AuthService userService;
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
      }

      @Test
      void signup_should_fail_no_body() throws Exception {

            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_content_type_not_set() throws Exception {
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
      }

      @ParameterizedTest()
      @NullAndEmptySource
      @ValueSource(strings = { "ayushgmail.com", " " })
      void signup_should_fail_improper_email(String email) throws Exception {
            signupRequestPayload.setEmail(email);
            mvc.perform(mockSignupRequest)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest()
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void signup_should_fail_username(String username) throws Exception {
            signupRequestPayload.setUsername(username);
            mvc.perform(mockSignupRequest)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void signup_should_fail_password(String password) throws Exception {
            signupRequestPayload.setPassword(password);
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void signup_should_fail_email_and_password(String value) throws Exception {
            signupRequestPayload.setPassword(value);
            signupRequestPayload.setEmail(value);
            MockHttpServletRequestBuilder request = mockSignupRequest
                        .content(asJsonString(signupRequestPayload));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_not_succeed_user_already_exists() throws Exception {
            doThrow(new InvalidCredentialsException())
                        .when(userService).signup(signupRequestPayload.toUser());
            mvc.perform(mockSignupRequest)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_succeed() throws Exception {
            mvc.perform(mockSignupRequest)
                        .andExpect(MockMvcResultMatchers.status().isCreated());
            verify(userService).signup(signupRequestPayload.toUser());
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
