package com.example.benomad.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "id")
    private User user;

    @Column(nullable = false,length = 2000)
    private String body;

    @ManyToMany
    @JoinTable(name = "comment_likes",
            joinColumns = @JoinColumn(columnDefinition = "comment_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(columnDefinition = "user_id",
                    referencedColumnName = "id")
    )
    private Set<User> likedUsers;

    @Column(name = "creation_date")
    private LocalDate creationDate;
}
