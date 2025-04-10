package com.example.BikeWeb.repository;

import com.example.BikeWeb.model.FavSpots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavSpotsRepository extends JpaRepository<FavSpots, Long> {
}
