package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.exceptions.InvalidCredentialsException;
import com.whatsapp.profile_service.exceptions.InvalidPasswordException;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repository.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

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
public class AuthServiceTest {
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      @Mock
      private JwtUtils jwtUtils;
      
      private AuthService underTest;
      private SignupRequest signupRequestPayload;
      
      private static final String USERNAME = "username";
      private static final String PASSWORD = "PASSWORD";
      private static final String EMAIL = "ayush@gmail.com";

      @Before
      public void setUp() {
            underTest = new AuthService(repository, encoder, jwtUtils);
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
            assertEquals(USERNAME, savedUser.getUsername());
      }

      @Test
      public void login_should_not_succeed_user_does_not_already_exist() throws Exception {
            //arrange
            when(repository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(null));
            //act
            Executable action =() -> underTest.login(EMAIL, PASSWORD);
            //assert
            assertThrowsExactly(UserNotFoundException.class,action);
            verify(repository).findByEmail(EMAIL);

      }

      @Test
      public void login_should_not_succeed_invalid_password() throws Exception {
            //arrange
            User user = new User();
            user.setEmail(EMAIL);
            user.setPassword(PASSWORD);

            when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(encoder.matches(PASSWORD, PASSWORD)).thenReturn(false);
            //act
            Executable action = () -> underTest.login(EMAIL, PASSWORD);
            //assert
            assertThrowsExactly(InvalidPasswordException.class, action);
            verify(repository).findByEmail(EMAIL);
            verify(encoder).matches(PASSWORD, PASSWORD);
      }

      @Test
      public void login_should_succeed() throws Exception {
            //arrange
            User user = new User();
            user.setEmail(EMAIL);
            user.setPassword(PASSWORD);

            when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(encoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
            when(jwtUtils.generateToken(user)).thenReturn("token");
            //act
            String jwt =underTest.login(EMAIL, PASSWORD);
            //assert
            assertEquals("token", jwt);
            verify(repository).findByEmail(EMAIL);
            verify(encoder).matches(PASSWORD, PASSWORD);
            verify(jwtUtils).generateToken(user);

      }
}
