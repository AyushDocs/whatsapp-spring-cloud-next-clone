package com.whatsapp.authentication.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.whatsapp.authentication.dto.SignupRequest;
import com.whatsapp.authentication.exceptions.InvalidCredentialsException;
import com.whatsapp.authentication.models.User;
import com.whatsapp.authentication.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      
      private SignupService underTest;
      private SignupRequest signupRequestPayload;
      
      private static final String USERNAME = "username";
      private static final String PASSWORD = "PASSWORD";
      private static final String EMAIL = "ayush@gmail.com";

      @Before
      public void setUp() {
            underTest = new SignupService(repository, encoder);
            signupRequestPayload = SignupRequest.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .username(USERNAME)
                        .build();
      }
     
      @Test
      public void signup_should_not_succeed_user_already_exists() throws Exception {
            //arrange
            when(repository.existsByEmail(EMAIL)).thenReturn(true);
            //act
            Executable action = () -> underTest.signup(signupRequestPayload.toUser());
            // assert
            assertThrowsExactly(InvalidCredentialsException.class,action);
            verify(repository).existsByEmail(EMAIL);

      }

      @Test
      public void signup_should_succeed() throws Exception {
            //arrange
            when(repository.existsByEmail(EMAIL)).thenReturn(false);
            when(encoder.encode(PASSWORD)).thenReturn("encodedPassword");
            //act
            Executable action = () -> underTest.signup(signupRequestPayload.toUser());
            //assert
            assertDoesNotThrow(action);
            verify(repository).existsByEmail(EMAIL);
            verify(encoder).encode(PASSWORD);
            ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
            verify(repository).save(ac.capture());
            User savedUser = ac.getValue();
            assertNotNull(savedUser);
            assertEquals(EMAIL, savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());
      }

     
}
