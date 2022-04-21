package com.whatsapp.profile_service.repository;

import java.util.Optional;

import com.whatsapp.profile_service.dto.UserResponseDto;
import com.whatsapp.profile_service.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

      boolean existsByEmail(String email);

      Optional<User> findByEmail(String email);

      Optional<User> findByUuid(String uuid);

      @Query("SELECT new com.whatsapp.profile_service.dto.UserResponseDto(u.email,u.imgUrl,u.username,u.uuid) "
                  + "FROM User u "
                  + "WHERE u.username LIKE %?1% OR u.email LIKE %?1%")
      Page<UserResponseDto> findPossibleFriends(String text,Pageable pageable);

}
