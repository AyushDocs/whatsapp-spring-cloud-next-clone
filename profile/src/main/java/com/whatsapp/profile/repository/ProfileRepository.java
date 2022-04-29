package com.whatsapp.profile.repository;

import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.models.Profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

      @Query("SELECT new com.whatsapp.profile.dto.UserResponseDto(p.email,p.imgUrl,p.username,p.uuid) "
                  + "FROM Profile p "
                  + "WHERE p.username LIKE %?1% OR p.email LIKE %?1%")
      Page<UserResponseDto> findPossibleFriends(String text,Pageable pageable);

}
