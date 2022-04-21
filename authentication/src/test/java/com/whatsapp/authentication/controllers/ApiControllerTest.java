package com.whatsapp.authentication.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import com.whatsapp.authentication.utils.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(ApiController.class)
@ExtendWith(MockitoExtension.class)
class ApiControllerTest {
      @Autowired
      private ApiController controller;
      @Autowired
      private MockMvc mvc;
      @MockBean
      private JwtUtils jwtUtils;

      @Test
      void should_not_get_user_roles() throws Exception {
            // arrange
            MockHttpServletRequestBuilder request = post("/api/v1/users/getRoles");
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isBadRequest());

      }

      @Test
      void should_get_1_user_role() throws Exception {
            // arrange
            when(jwtUtils.extractRoles("secret")).thenReturn(new String[] { "USER"});

            Cookie authCookie = new Cookie("token", "secret");
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            MockHttpServletRequestBuilder request = post("/api/v1/users/getRoles")
                        .cookie(authCookie);
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.data").exists());
            result.andExpect(jsonPath("$.data").isArray());
            result.andExpect(jsonPath("$.data[0]").value("USER"));
      }
      @Test
      void should_get_all_user_role() throws Exception {
            // arrange
            when(jwtUtils.extractRoles("secret")).thenReturn(new String[]{"USER","ADMIN"});
            Cookie authCookie = new Cookie("token", "secret");
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            MockHttpServletRequestBuilder request = post("/api/v1/users/getRoles")
                        .cookie(authCookie);
            // act
            ResultActions result = mvc.perform(request);
            // assert
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.data").exists());
            result.andExpect(jsonPath("$.data").isArray());
            result.andExpect(jsonPath("$.data[0]").value("USER"));
            result.andExpect(jsonPath("$.data[1]").value("ADMIN"));
      }
}
