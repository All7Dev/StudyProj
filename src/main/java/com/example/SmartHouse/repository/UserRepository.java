package com.example.SmartHouse.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartHouse.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
