package com.example.benomad.entity;

import com.example.benomad.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "author_id",
                referencedColumnName = "id")
    private User author;

    @ManyToMany
    @JoinTable(name = "blog_likes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedUsers;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    private Status status;

}
