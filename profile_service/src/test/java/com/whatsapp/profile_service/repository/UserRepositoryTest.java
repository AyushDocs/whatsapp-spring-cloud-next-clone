package com.whatsapp.profile_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepositoryTest {
      @Autowired
      private UserRepository userRepository;
      @Autowired
      private TestEntityManager em;

      @BeforeEach
      void setup() {
            em.clear();
      }

      @Test
      void user_should_exist_by_email() {
            User user = User.builder()
                        .email("ayush@g.com")
                        .password("password")
                        .id(1l)
                        .build();
            em.persistAndFlush(user);
            assertTrue(userRepository.existsByEmail("ayush@g.com"));
      }

      @Test
      void user_should_not_exist_by_email() {
            assertFalse(userRepository.existsByEmail("ayush@g.com"));
      }

      @Test
      void should_find_user_by_email() {
            User userInDb = User.builder()
                        .email("ayush@g.com")
                        .password("password")
                        .id(1l)
                        .build();
            em.persistAndFlush(userInDb);
            Optional<User> userOpt = userRepository.findByEmail("ayush@g.com");
            userOpt.ifPresentOrElse(user -> {
                  assertEquals("ayush@g.com", user.getEmail());
                  assertEquals("password", user.getPassword());
            }, () -> {
                  fail("user is null");
            });
      }

      @Test
      void should_not_find_user_by_email() {
            Optional<User> userOpt = userRepository.findByEmail("ayush@g.com");
            userOpt.ifPresent(user -> fail("user is null"));
      }
}
