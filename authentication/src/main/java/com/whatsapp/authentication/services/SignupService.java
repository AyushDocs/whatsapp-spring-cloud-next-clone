package com.whatsapp.authentication.services;

import com.whatsapp.authentication.exceptions.InvalidCredentialsException;
import com.whatsapp.authentication.models.User;
import com.whatsapp.authentication.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;
      private final ProfileService profileService;
      
      public void signup(User user) {
            validateUserForSignup(user);
            modifyUserForSignup(user);
            saveUser(user);
            //TODO: send a request to profile service and add user there
      }
      private void validateUserForSignup(User user) {
            if (userRepository.existsByEmail(user.getEmail()))
                  throw new InvalidCredentialsException();
      }
      
      private void modifyUserForSignup(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
      }

      private void saveUser(User user) {
            userRepository.save(user);
            profileService.saveUser(user);
      }
}
