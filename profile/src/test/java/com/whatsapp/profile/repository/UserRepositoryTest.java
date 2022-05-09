package com.whatsapp.profile.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.whatsapp.profile.dto.FindUserDto;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.models.Profile;

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
      private ProfileRepository underTest;
      @Autowired
      private TestEntityManager em;

      @BeforeEach
      void setup() {
            em.clear();
      }

      @Test
      void should_not_find_by_email() {

            FindUserDto dto = underTest.findByEmail("email");

            assertNull(dto);
      }

      @Test

      void should_find_by_email() {
            em.persistAndFlush(new Profile("email", "name", "url"));

            FindUserDto dto = underTest.findByEmail("email");

            assertNotNull(dto);
            assertEquals("name", dto.getUsername());
            assertEquals("url", dto.getImgUrl());
            assertEquals("email", dto.getEmail());
      }

      @Test

      void should_not_find_friends() {

            Page<UserResponseDto> res = underTest.findPossibleFriends("nam", PageRequest.of(0, 2));

            assertNotNull(res);
            assertEquals(0, res.getNumberOfElements());
      }

      @Test

      void should_find_friends() {
            em.persistAndFlush(new Profile("email", "name", "url"));

            Page<UserResponseDto> res = underTest.findPossibleFriends("nam", PageRequest.of(0, 2));

            assertNotNull(res);
            assertEquals(1, res.getNumberOfElements());
            assertEquals("name", res.getContent().get(0).getUsername());
            assertEquals("url", res.getContent().get(0).getImgUrl());
            assertEquals("email", res.getContent().get(0).getEmail());
      }

      @Test

      void should_not_update_profile() {
            Profile savedProfile = em
                        .persistAndFlush(new Profile("email", "name", ""));

            underTest.updateImageUrlByUserUuid("url", "uuid");

            Profile foundProfile = em.find(Profile.class, savedProfile.getId());
            assertEquals("", foundProfile.getImgUrl());
      }

      @Test
      void should_update_profile() {
            Profile savedProfile = em
                        .persistAndFlush(new Profile("email", "name", ""));

            underTest.updateImageUrlByUserUuid("url", savedProfile.getUuid());

            Profile foundProfile = em.find(Profile.class, savedProfile.getId());
            assertEquals("url", foundProfile.getImgUrl());
      }
}
