package com.whatsapp.authentication.services;

import com.whatsapp.authentication.exceptions.InvalidPasswordException;
import com.whatsapp.authentication.exceptions.UserNotFoundException;
import com.whatsapp.authentication.models.User;
import com.whatsapp.authentication.repository.UserRepository;
import com.whatsapp.authentication.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;
      private final JwtUtils jwtUtils;

      public String login(String email, String password) {
            User user = loadUserByEmail(email);
            validateUserLogin(password, user);
            return jwtUtils.generateToken(user);
      }

      private void validateUserLogin(String password, User user) {
            if (!passwordEncoder.matches(password, user.getPassword()))
                  throw new InvalidPasswordException();
      }

      private User loadUserByEmail(String email) {
            return userRepository
                        .findByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
      }
}
