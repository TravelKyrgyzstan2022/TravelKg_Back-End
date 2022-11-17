
package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.example.benomad.security.domain.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"favorite_places", "roles"})
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "First name can't be null or empty")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name can't be null or empty")
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Email can't be null or empty")
    @Email(message = "Email is invalid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Role can't be null or empty")
    private Set<Role> roles = new HashSet<Role>();
    
    @JsonProperty("favorite_places")
    private List<PlaceDTO> placeDTOS;

//    @JsonIgnore
//    private Integer resetPasswordCode;
//
//    @JsonIgnore
//    private Instant codeExpirationDate;
//
//    @JsonIgnore
//    private boolean pwdChangeRequired;
}
