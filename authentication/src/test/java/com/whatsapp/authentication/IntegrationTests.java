package com.whatsapp.authentication;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.authentication.dto.LoginRequest;
import com.whatsapp.authentication.dto.SignupRequest;
import com.whatsapp.authentication.models.User;
import com.whatsapp.authentication.services.SignupService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApplication.class)
@AutoConfigureMockMvc
class IntegrationTests {
      @Autowired
      private MockMvc mvc;
      @Autowired
      private SignupService signUpService;
      
      private static final ObjectMapper MAPPER = new ObjectMapper();
      private static final String SIGNUP_URL = "/api/v1/authentication/signup";
      private static final String LOGIN_URL = "/api/v1/authentication/login";
      
      private static final String NAME = "ayush";
      private static final String PASSWORD = "123456";
      private static final String EMAIL = "a@g.com";
      @Test
      void signup_should_happen() throws Exception {
            SignupRequest requestEntity = new SignupRequest(EMAIL, PASSWORD, NAME);
            MockHttpServletRequestBuilder request = post(SIGNUP_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(requestEntity));

            ResultActions result = mvc.perform(request);

            result.andExpect(status().isCreated());
      }

      private String asJsonString(Object obj) throws JsonProcessingException {
            return MAPPER.writeValueAsString(obj);
      }

      @Test
      void login_should_happen() throws Exception {
            createUser();
            LoginRequest requestEntity = new LoginRequest(EMAIL, PASSWORD);

            MockHttpServletRequestBuilder request = post(LOGIN_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(requestEntity));
            ResultActions result = mvc.perform(request);

            result.andExpect(status().isOk());
            result.andExpect(MockMvcResultMatchers.cookie().exists("token"));
            result.andExpect(MockMvcResultMatchers.cookie().httpOnly("token",true));
            result.andExpect(MockMvcResultMatchers.cookie().secure("token",true));
      }

      private void createUser() {
            User user=new User();
            user.setEmail(EMAIL);
            user.setPassword(PASSWORD);
            signUpService.signup(user);
      }

}
