package com.example.benomad.repository;

import com.example.benomad.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
/*
    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('RESTAURANT', 'CAFE', 'COFFEE_HOUSE')")
    List<Place> findPlacesToEatFood();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('HOTEL', 'HOSTEL')")
    List<Place> findPlacesToStay();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type = 'ENTERTAINMENT'")
    List<Place> findPlacesToEntertain();

    @Query(nativeQuery = true,
            value = "SELECT * FROM places WHERE place_type IN ('HIKING', 'RIVER_LAKE', 'PARK', 'CULTURE')")
    List<Place> findPlacesToSeeAndTry();
*/

    @Query(value = "SELECT last_value FROM places_id_seq", nativeQuery = true)
    Long getLastValueOfSequence();
}
