package com.example.benomad.dto;

import com.example.benomad.security.domain.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"favorite_places", "roles"})
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;

    @JsonProperty(value = "registration_date", access = JsonProperty.Access.READ_ONLY)
    private String registrationDate;

    @JsonProperty(value = "last_visit_datetime", access = JsonProperty.Access.READ_ONLY)
    private String lastVisitDateTime;

    private Set<Role> roles;

    @JsonProperty(value = "is_activated", access = JsonProperty.Access.READ_ONLY)
    private Boolean isActivated;

    @JsonProperty(value = "is_deleted", access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(value = "deletion_info", access = JsonProperty.Access.READ_ONLY)
    private DeletionInfoDTO deletionInfoDTO;

    @JsonProperty(value = "favorite_places",access = JsonProperty.Access.READ_ONLY)
    private Set<PlaceDTO> placeDTOS;

    @JsonProperty(value = "image_url",access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;

}
