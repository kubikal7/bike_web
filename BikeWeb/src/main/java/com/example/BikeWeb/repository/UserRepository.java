package com.example.BikeWeb.repository;

import com.example.BikeWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByToken(String token);

    Optional<User> findByToken(String token);
}
