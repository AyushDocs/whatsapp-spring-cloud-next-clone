package com.whatsapp.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.HttpCookie;
import java.util.List;

import com.whatsapp.authentication.config.JwtConfig;
import com.whatsapp.authentication.dto.LoginRequest;
import com.whatsapp.authentication.exceptions.InvalidCredentialsException;
import com.whatsapp.authentication.services.LoginService;
import com.whatsapp.authentication.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@WebMvcTest(LoginController.class)
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
      @MockBean
      private LoginService loginService;
      @MockBean
      private JwtConfig jwtConfig;
      @MockBean
      private JwtUtils jwtUtils;
      @Autowired
      private LoginController loginController;

      private LoginRequest loginRequestPayload;

      private static final String PASSWORD = "secret";
      private static final String EMAIL = "ayush@gmail.com";

      @BeforeEach
      void init() {
            loginRequestPayload = LoginRequest.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            when(jwtUtils.verifyToken(anyString())).thenReturn(true);
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { "ayushgmail.com", " " })
      void login_should_fail_improper_email(String email) throws Exception {
            // act
            loginRequestPayload.setEmail(email);
            Executable action = () -> loginController.login(loginRequestPayload);
            // assert
            assertThrows(IllegalArgumentException.class, action);
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = { " " })
      void login_should_fail_no_password(String password) throws Exception {
            // act
            loginRequestPayload.setPassword(password);
            Executable action = () -> loginController.login(loginRequestPayload);
            // assert
            assertThrows(IllegalArgumentException.class, action);
      }

      @Test
      void login_should_fail_no_email_and_password() throws Exception {
            // act
            loginRequestPayload.setEmail(null);
            loginRequestPayload.setPassword(null);
            Executable action = () -> loginController.login(loginRequestPayload);
            // assert
            assertThrows(IllegalArgumentException.class, action);

      }

      @Test
      void login_should_not_succeed_invalid_user() throws Exception {
            // arrange
            when(loginService.login(EMAIL, PASSWORD)).thenThrow(new InvalidCredentialsException());
            // act
            Executable action = () -> loginController.login(loginRequestPayload);
            // assert
            assertThrows(InvalidCredentialsException.class, action);
            verify(loginService).login(EMAIL, PASSWORD);
      }

      @Test
      void login_should_succeed() throws Exception {
            // arrange
            when(jwtConfig.getCookieName()).thenReturn("token");
            when(loginService.login(EMAIL, PASSWORD)).thenReturn("jwt");
            // act
            ResponseEntity<Void> response = loginController.login(loginRequestPayload);
            // assert
            verify(loginService).login(EMAIL, PASSWORD);
            List<String> cookieStrings = response.getHeaders().get(HttpHeaders.SET_COOKIE);
            String tokenValue = cookieStrings
                        .stream()
                        .map(HttpCookie::parse)
                        .map(cookies -> cookies.get(0))
                        .filter(cookie -> cookie.getName().equals("token"))
                        .findFirst()
                        .get()
                        .getValue();
            assertEquals("jwt", tokenValue);
      }
}
