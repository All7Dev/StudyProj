package com.example.SmartHouse.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartHouse.entity.Role;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
