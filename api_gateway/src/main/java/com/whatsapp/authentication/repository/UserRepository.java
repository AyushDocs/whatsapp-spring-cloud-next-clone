package com.whatsapp.authentication.repository;

import java.util.Optional;

import com.whatsapp.authentication.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

      boolean existsByEmail(String email);

      Optional<User> findByEmail(String email);
}
