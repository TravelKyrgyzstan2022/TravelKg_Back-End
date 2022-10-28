package com.example.benomad.repository;

import com.example.benomad.entity.Place;
import com.example.benomad.entity.Rating;
import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository <Rating, Long> {
    List<Rating> findByPlace(Place place);
    List<Rating> findByUser(User user);
    Optional<Rating> findByPlaceAndUser(Place place, User user);

    @Query(value = "SELECT AVG(r.rating) FROM place_ratings r WHERE r.place_id = :placeId", nativeQuery = true)
    Double findAverageRatingByPlaceId(@Param("placeId") Long placeId);

    @Query(value = "SELECT COUNT(r) FROM place_ratings r WHERE r.place_id = :placeId", nativeQuery = true)
    Integer findRatingCountByPlaceId(@Param("placeId") Long placeId);
}
