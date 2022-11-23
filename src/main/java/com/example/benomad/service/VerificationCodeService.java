package com.example.benomad.service;

public interface VerificationCodeService {
    void newCode(String email, String code);
    boolean isCodeValid(String email, String code);
    void deleteCode(String email, String code);
}
