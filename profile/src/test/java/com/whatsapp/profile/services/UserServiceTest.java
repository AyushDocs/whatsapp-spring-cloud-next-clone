package com.whatsapp.profile.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.whatsapp.profile.dto.AddImageUrlDto;
import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.repository.ProfileRepository;
import com.whatsapp.profile.service.ProfileService;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
      @Mock
      private ProfileRepository repository;

      private ProfileService underTest;

      private static final String TEXT = "text";

      @Before
      public void setup() {
            underTest = new ProfileService(repository);
      }

      @Test
      public void should_find_users() {
            // arrange
            Page<UserResponseDto> users = new PageImpl<>(List.of());
            when(repository
                        .findPossibleFriends(anyString(),
                                    any(Pageable.class)))
                        .thenReturn(users);
            // act
            underTest.findUsersByText(TEXT, 0, 1);
            // assert
            verify(repository)
                        .findPossibleFriends(anyString(), any(Pageable.class));

      }

      @Test
      public void update_image_url() {
            AddImageUrlDto addImageUrlDto = new AddImageUrlDto("uuid","url");
            
            underTest.updateImageUrl(addImageUrlDto);

            verify(repository).updateImageUrlByUserUuid("url", "uuid");

      }
}
