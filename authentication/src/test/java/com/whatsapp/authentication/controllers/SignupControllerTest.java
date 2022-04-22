package com.whatsapp.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.whatsapp.authentication.config.JwtConfig;
import com.whatsapp.authentication.dto.SignupRequest;
import com.whatsapp.authentication.exceptions.InvalidCredentialsException;
import com.whatsapp.authentication.services.SignupService;
import com.whatsapp.authentication.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@WebMvcTest(SignupController.class)
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class SignupControllerTest {
      @MockBean
      private JwtConfig jwtConfig;
      @MockBean
      private SignupService signupService;
      @MockBean
      private JwtUtils jwtUtils;
      @Autowired
      private SignupController signupController;
      private SignupRequest signupRequestPayload;

      @BeforeEach
       void setUp() {
            signupRequestPayload = SignupRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .username("username")
                        .build();
            when(jwtUtils.verifyToken(anyString())).thenReturn(false);
            when(jwtConfig.getCookieName()).thenReturn("token");

      }

      @Test
       void signup_should_not_succeed_user_already_exists(){
            // arrange
            doThrow(new InvalidCredentialsException())
                        .when(signupService).signup(signupRequestPayload.toUser());
            // act
            Executable result = () -> signupController.signup(signupRequestPayload);
            // assert
            assertThrows(InvalidCredentialsException.class, result);
      }

      @Test
       void signup_should_succeed(){
            //act
            signupController.signup(signupRequestPayload);
            //assert
            verify(signupService).signup(signupRequestPayload.toUser());
      }
}
