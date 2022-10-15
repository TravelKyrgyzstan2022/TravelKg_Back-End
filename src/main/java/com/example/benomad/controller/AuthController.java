package com.example.benomad.controller;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return  "register";
    }


    @PostMapping("/signup")
    public String registre(UserDTO userDTO){
        userDetailsService.save(userDTO);
        return "login";
    }
}
