package com.whatsapp.profile_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.whatsapp.profile_service.dto.UserResponseDto;
import com.whatsapp.profile_service.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class UserRepositoryTest {
      @Autowired
      private UserRepository userRepository;
      @Autowired
      private TestEntityManager em;

      private static final String PASSWORD = "password";
      private static final String EMAIL = "ayush@g.com";

      @BeforeEach
      void setup() {
            em.clear();
      }

      @Test
      void user_should_exist_by_email() {
            saveUserInDb();
            assertTrue(userRepository.existsByEmail(EMAIL));
      }

      @Test
      void user_should_not_exist_by_email() {
            assertFalse(userRepository.existsByEmail(EMAIL));
      }

      @Test
      void should_find_user_by_email() {
            saveUserInDb();
            Optional<User> userOpt = userRepository.findByEmail(EMAIL);
            assertTrue(userOpt.isPresent());
            userOpt.ifPresent(user -> {
                  assertEquals(EMAIL, user.getEmail());
                  assertEquals(PASSWORD, user.getPassword());
            });
      }

      private void saveUserInDb() {
            User userInDb = new User();
            userInDb.setEmail(EMAIL);
            userInDb.setPassword(PASSWORD);
            em.persistAndFlush(userInDb);
      }

      @Test
      void should_not_find_user_by_email() {
            Optional<User> userOpt = userRepository.findByEmail(EMAIL);
            assertTrue(userOpt.isEmpty());
      }

      @Test
      void should_not_find_all_users() {
            saveUserInDb();
            Page<UserResponseDto> res = userRepository.findPossibleFriends("ayush", PageRequest.of(0, 1));
            UserResponseDto dto = res.getContent().get(0);
            assertEquals(EMAIL, dto.getEmail());
            assertNotNull(dto.getUuid());
      }
}
