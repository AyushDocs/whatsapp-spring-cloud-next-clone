package com.whatsapp.profile_service.services;

import com.whatsapp.profile_service.exceptions.InvalidCredentialsException;
import com.whatsapp.profile_service.exceptions.InvalidPasswordException;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repository.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public void signup(User user) {
        validateUserForSignup(user);
        modifyUserForSignup(user);
        saveUser(user);
    }

    private void modifyUserForSignup(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
    }

    private void validateUserForSignup(User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new InvalidCredentialsException();
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

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
