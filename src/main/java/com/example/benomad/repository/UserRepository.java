package com.example.benomad.repository;

import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByLogin(String login);

    @Query(value = "SELECT last_value FROM users_id_seq", nativeQuery = true)
    Long getLastValueOfArticleSequence();
}
