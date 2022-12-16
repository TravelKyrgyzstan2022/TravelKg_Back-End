package com.example.benomad.repository;

import com.example.benomad.entity.Place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = "select * from places p where p.place_category in :categories and p.place_type in :types and p.region in :regions", nativeQuery = true,
            countQuery ="select count(*) from places p where p.place_category in :categories and p.place_type in :types and p.region in :regions")
    Page<Place> findPlacesByPlaceCategoriesAndPlaceTypes(@Param("categories") List<String> categories, @Param("types") List<String> types,@Param("regions") List<String> regions,Pageable pageable);
    
    @Query(value = "" +
            "SELECT p.* FROM places p JOIN " +
            "(SELECT t.place_id AS place_id, AVG(t.rating) AS avg_rating FROM place_ratings t GROUP BY t.place_id) r " +
            "ON p.id = r.place_id WHERE p.place_category in :categories ORDER BY r.avg_rating DESC LIMIT :limit",nativeQuery = true)
    List<Place>  findTop5PlacesForPlaceCategory(@Param("categories") List<String> categories,@Param("limit") Integer limit);
}
