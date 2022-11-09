package com.example.benomad.controller;

import com.example.benomad.entity.User;
import com.example.benomad.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class RegistrationController {
    @Autowired
    private UserServiceImpl userSevice;


    @PostMapping("/registration")
    public String addUser(@RequestBody User user) {
        if (!userSevice.addUser(user)) {
            return "User exists!";
        }

        return "Successful registration";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userSevice.activateUser(code);

        if (isActivated) {
            return "User successfully activated";
        }
        return "Activation code is not found!";
    }
}
