package com.example.benomad.repository;

import com.example.benomad.entity.Place;
import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository <Plan, Long> {
    List<Plan> findByUser(User user);
    List<Plan> findByPlace(Place place);

}
