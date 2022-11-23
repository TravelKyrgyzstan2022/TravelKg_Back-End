package com.example.benomad.repository;

import com.example.benomad.entity.User;
import com.example.benomad.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository <VerificationCode, Long> {
    List<VerificationCode> findByUser(User user);
    Optional<VerificationCode> findByUserAndCode(User user, String code);
}
