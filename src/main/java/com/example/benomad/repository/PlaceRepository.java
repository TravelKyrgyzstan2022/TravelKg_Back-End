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

    @Query(value = "SELECT last_value FROM places_id_seq", nativeQuery = true)
    Long getLastValueOfSequence();

    @Query(value = "select * from places p where p.place_category in :categories and p.place_type in :types and p.region in :regions", nativeQuery = true,
            countQuery ="select count(*) from places p where p.place_category in :categories and p.place_type in :types and p.region in :regions")
    Page<Place> findPlacesByPlaceCategoriesAndPlaceTypes(@Param("categories") List<String> categories, @Param("types") List<String> types,@Param("regions") List<String> regions,Pageable pageable);
}
