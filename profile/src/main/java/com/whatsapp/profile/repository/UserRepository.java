package com.whatsapp.profile.repository;

import com.whatsapp.profile.dto.UserResponseDto;
import com.whatsapp.profile.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

      @Query("SELECT new com.whatsapp.profile.dto.UserResponseDto(u.email,u.imgUrl,u.username,u.uuid) "
                  + "FROM User u "
                  + "WHERE u.username LIKE %?1% OR u.email LIKE %?1%")
      Page<UserResponseDto> findPossibleFriends(String text,Pageable pageable);

}
