package com.example.benomad.repository;

import com.example.benomad.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('RESTAURANT', 'CAFE', 'TEAHOUSE')")
    List<Place> findPlacesToEatFood();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type = 'HOTEL'")
    List<Place> findPlacesToStay();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('CINEMA')")
    List<Place> findPlacesToEntertain();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('MOUNTAIN')")
    List<Place> findPlacesToSeeAndTry();
}
