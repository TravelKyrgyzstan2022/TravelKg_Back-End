package com.example.benomad.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;


    public void send(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendForgotPasswordCode(String email, String code){
        String mailMessage = String.format("""
                Your email verification code for Benomad account:
                
                %s

                Expiration time - 10 minutes.""", code);
        send(email, "Reset password: email verification", mailMessage);
    }

    public void sendActivationCode(String email, String code){
        String mailMessage = String.format("""
                Your activation code for Benomad account is <%s>.

                Expiration time - 10 minutes.""", code);
        send(email, "Activation code", mailMessage);
    }
}
