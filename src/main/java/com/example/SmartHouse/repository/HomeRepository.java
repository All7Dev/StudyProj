package com.example.SmartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Home;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    // JpaRepository уже содержит методы save, findById, findAll, deleteById и т.д.
}