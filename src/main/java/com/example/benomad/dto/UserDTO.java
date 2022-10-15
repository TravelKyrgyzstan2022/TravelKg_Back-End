package com.example.benomad.dto;

import com.example.benomad.entity.Role;
import com.example.benomad.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String login;

    private String password;

    private String phoneNumber;

    private String email;

    private Role role;

    private Status status;
}
