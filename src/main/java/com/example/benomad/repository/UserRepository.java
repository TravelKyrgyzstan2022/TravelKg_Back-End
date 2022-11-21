
package com.example.benomad.repository;

import com.example.benomad.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    
    List<User> findByFirstName(String firstName);

    List<User> findAllByOrderByIdAsc();
    
    List<User> findByLastName(String lastName);
    
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
}
