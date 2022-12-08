package com.example.benomad.entity;

import com.example.benomad.enums.ReviewStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    private Boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            columnDefinition = "deleted_content_info_id",
            referencedColumnName = "id"
    )
    private DeletionInfo deletionInfo;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @OneToMany
    @JoinTable(name = "blog_comments",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments;

    @Column(name = "image_urls")
    @ElementCollection(targetClass=String.class)
    private List<String> imageUrls = new ArrayList<>();
}
