package com.example.benomad.repository;

import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository <Plan, Long> {
    List<Plan> findByUser(User user);

    @Query(value = "SELECT * FROM plans WHERE date = :date AND user_id = :userId", nativeQuery = true)
    List<Plan> findByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM plans WHERE (SELECT EXTRACT(YEAR FROM date)) = " +
            "(SELECT EXTRACT(YEAR FROM CAST(:date AS DATE))) AND user_id = :userId", nativeQuery = true)
    List<Plan> findByYear(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM plans WHERE EXTRACT(MONTH FROM date) = " +
            "(SELECT EXTRACT(MONTH FROM CAST(:date AS DATE))) AND user_id = :userId", nativeQuery = true)
    List<Plan> findByMonth(@Param("userId") Long userId, @Param("date") LocalDate date);
}
