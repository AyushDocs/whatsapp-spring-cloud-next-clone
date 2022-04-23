package com.whatsapp.authentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.whatsapp.authentication.exceptions.InvalidPasswordException;
import com.whatsapp.authentication.exceptions.UserNotFoundException;
import com.whatsapp.authentication.models.User;
import com.whatsapp.authentication.repository.UserRepository;
import com.whatsapp.authentication.utils.JwtUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      @Mock
      private JwtUtils jwtUtils;

      private LoginService underTest;

      private static final String PASSWORD = "PASSWORD";
      private static final String EMAIL = "ayush@gmail.com";
      
      @Before
      public void setUp() {
            underTest = new LoginService(repository, encoder,jwtUtils);
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
