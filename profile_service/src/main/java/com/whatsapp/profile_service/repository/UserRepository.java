package com.whatsapp.profile_service.repository;

import java.util.Optional;

import com.whatsapp.profile_service.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

      boolean existsByEmail(String email);

      Optional<User> findByEmail(String email);

      Optional<User> findByUuid(String uuid);

}
