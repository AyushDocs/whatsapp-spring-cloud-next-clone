package com.whatsapp.profile.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepositoryTest {
      @Autowired
      private ProfileRepository userRepository;
      @Autowired
      private TestEntityManager em;

      private static final String PASSWORD = "password";
      private static final String EMAIL = "ayush@g.com";

      @BeforeEach
      void setup() {
            em.clear();
      }

}
