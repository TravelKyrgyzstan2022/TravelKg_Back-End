
package com.example.benomad.repository;

import com.example.benomad.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @Query(value = "SELECT * FROM users WHERE id IN (SELECT DISTINCT author_id FROM blogs)", nativeQuery = true)
    List<User> findBlogAuthors();

    @Query(value =
            "SELECT * FROM users WHERE id IN (SELECT DISTINCT author_id FROM blogs) " +
                    "AND first_name LIKE '%:firstName%'", nativeQuery = true)
    List<User> findBlogAuthorsByFirstName(@Param("firstName") String firstName);

    @Query(value =
            "SELECT * FROM users WHERE id IN (SELECT DISTINCT author_id FROM blogs) " +
                    "AND last_name LIKE '%:lastName%'", nativeQuery = true)
    List<User> findBlogAuthorsByLastName(@Param("lastName") String lastName);

    @Query(value = "SELECT * FROM users WHERE id IN (SELECT DISTINCT author_id FROM blogs) " +
            "AND first_name LIKE '%:firstName%' AND last_name LIKE '%:lastName%'", nativeQuery = true)
    List<User> findBlogAuthorsByFirstNameAndLastName(@Param("firstName") String firstName,
                                                     @Param("lastName") String lastName);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
