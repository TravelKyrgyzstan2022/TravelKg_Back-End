package com.example.benomad.entity;

import com.example.benomad.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;
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

    @OneToMany
    @JoinTable(name = "blog_comments",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments;

    @Getter(AccessLevel.NONE)
    @Column(name = "image_url")
    private String imageUrl;

    public Optional<String> getImageUrl() {
        return Optional.ofNullable(imageUrl);
    }
}
