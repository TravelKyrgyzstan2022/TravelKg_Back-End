package com.example.benomad.repository;

import com.example.benomad.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlaceRepository  extends JpaRepository<Place,Long> {

    }
