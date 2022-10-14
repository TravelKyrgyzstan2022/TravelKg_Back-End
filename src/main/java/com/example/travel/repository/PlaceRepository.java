package com.example.travel.repository;

import com.example.travel.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository  extends JpaRepository<Place,Long> {
}
