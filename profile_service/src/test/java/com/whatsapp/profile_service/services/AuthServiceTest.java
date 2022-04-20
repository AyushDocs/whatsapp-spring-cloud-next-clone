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
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repository.UserRepository;
import com.whatsapp.profile_service.services.AuthService;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
class AuthServiceTest {
      private AuthService underTest;
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      @Mock
      private JwtUtils jwtUtils;
      private SignupRequest signupRequestPayload;

      @BeforeEach
      void setUp() {
            underTest = new AuthService(repository, encoder, jwtUtils);
            signupRequestPayload = SignupRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .username("username")
                        .build();
      }

      @Test
      void signup_should_not_succeed_user_already_exists() throws Exception {
            when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
            assertThrowsExactly(
                        InvalidCredentialsException.class,
                        () -> underTest.signup(signupRequestPayload.toUser()));
            verify(repository).existsByEmail("ayush@gmail.com");

      }

      @Test
      void signup_should_succeed() throws Exception {
            when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
            when(encoder.encode("secret")).thenReturn("encodedPassword");
            assertDoesNotThrow(() -> underTest.signup(signupRequestPayload.toUser()));
            verify(repository).existsByEmail("ayush@gmail.com");
            verify(encoder).encode("secret");
            ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
            verify(repository).save(ac.capture());
            User savedUser = ac.getValue();
            assertNotNull(savedUser);
            assertEquals("ayush@gmail.com", savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());
            assertEquals("username", savedUser.getUsername());
      }

      @Test
      void login_should_not_succeed_user_does_not_already_exist() throws Exception {
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.ofNullable(null));
            assertThrowsExactly(InvalidCredentialsException.class,
                        () -> underTest.login("ayush@gmail.com", "secret"));
            verify(repository).findByEmail("ayush@gmail.com");

      }

      @Test
      void login_should_not_succeed_invalid_password() throws Exception {
            User user = User.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.of(user));
            when(encoder.matches("secret", "secret")).thenReturn(false);
            assertThrowsExactly(InvalidCredentialsException.class, () -> underTest.login("ayush@gmail.com", "secret"));
            verify(repository).findByEmail("ayush@gmail.com");
            verify(encoder).matches("secret", "secret");
      }

      @Test
      void login_should_succeed() throws Exception {
            User user = User.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.of(user));
            when(encoder.matches("secret", "secret")).thenReturn(true);
            when(jwtUtils.generateToken(user)).thenReturn("token");
            assertEquals("token", underTest.login("ayush@gmail.com", "secret"));
            verify(repository).findByEmail("ayush@gmail.com");
            verify(encoder).matches("secret", "secret");
            verify(jwtUtils).generateToken(user);

      }
}
