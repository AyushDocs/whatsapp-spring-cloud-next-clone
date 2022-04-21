package com.whatsapp.profile.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.whatsapp.profile.models.User;
import com.whatsapp.profile.repository.UserRepository;

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

      private static final String PASSWORD = "password";
      private static final String EMAIL = "ayush@g.com";

      @BeforeEach
      void setup() {
            em.clear();
      }

}
