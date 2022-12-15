package com.example.benomad.repository;

import com.example.benomad.entity.User;
import com.example.benomad.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository <VerificationCode, Long> {
    Optional<VerificationCode> findByUserAndCode(User user, String code);
}
