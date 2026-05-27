package com.example.SmartHouse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Token;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
