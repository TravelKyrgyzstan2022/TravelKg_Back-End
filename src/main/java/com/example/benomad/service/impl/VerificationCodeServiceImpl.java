package com.example.benomad.service.impl;

import com.example.benomad.entity.VerificationCode;
import com.example.benomad.exception.InvalidVerificationCodeException;
import com.example.benomad.repository.VerificationCodeRepository;
import com.example.benomad.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository codeRepository;
    private final UserServiceImpl userService;

    @Override
    public void newCode(String email, String code) {
        VerificationCode verificationCode = codeRepository.findByUserAndCode(
                userService.getUserEntityByEmail(email), code).orElse(
                VerificationCode.builder()
                        .user(userService.getUserEntityByEmail(email))
                        .code(code)
                        .build()
        );
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        codeRepository.save(verificationCode);
    }

    @Override
    public boolean isCodeValid(String email, String code) {
        VerificationCode verificationCode = codeRepository.findByUserAndCode(
                userService.getUserEntityByEmail(email), code).orElseThrow(
                        () -> new InvalidVerificationCodeException("Invalid verification code"));
        if(isExpired(verificationCode)){
            throw new InvalidVerificationCodeException("Verification code has expired at " +
                    verificationCode.getExpiryDate());
        }
        return true;
    }

    @Override
    public void deleteCode(String email, String code) {
        VerificationCode verificationCode = codeRepository.findByUserAndCode(
                userService.getUserEntityByEmail(email), code).orElseThrow(
                () -> new InvalidVerificationCodeException("BRUH"));
        codeRepository.delete(verificationCode);
    }

    private boolean isExpired(VerificationCode verificationCode){
        LocalDateTime expiryDate = verificationCode.getExpiryDate();
        return (expiryDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) < 0;
    }
}
