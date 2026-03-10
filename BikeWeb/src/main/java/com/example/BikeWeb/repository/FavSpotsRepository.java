package com.example.BikeWeb.repository;

import com.example.BikeWeb.model.FavSpots;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavSpotsRepository extends JpaRepository<FavSpots, Long> {
    Optional<FavSpots> findByName(String name);
    Optional<FavSpots[]> findAllByUserId(long user_id);
    void deleteByName(String name);

    @Transactional
    void deleteByNameAndUserIdAndSpotId(String name, long user_id, String spot_id);

    void deleteAllByUserId(Long userId);
}
