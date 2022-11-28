
package com.example.benomad.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.benomad.security.domain.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_phone_number", columnNames = "phone_number")
        }
)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    private boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            columnDefinition = "deleted_content_info_id",
            referencedColumnName = "id"
    )
    private DeletionInfo deletionInfo;

    @Column(name = "is_activated")
    @JsonIgnore
    private boolean isActivated;

    @Column(name = "last_visit_date")
    private LocalDateTime lastVisitDate;

    //fixme
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", foreignKey = @ForeignKey(name = "fk_users_role"))
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "favorite_places",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id"))
    private List<Place> places;

    @ManyToMany(mappedBy = "likedUsers")
    private List<Blog> blogs;

    public void addRole(Role role){
        roles.add(role);
    }

    public void removeRole(Role role){
        roles.remove(role);
    }
}
