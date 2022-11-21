package com.example.benomad.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 10000)
    private String body;

    @Getter(AccessLevel.NONE)
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(
            columnDefinition = "user_id",
            referencedColumnName = "id"
    )
    private User user;

    public Optional<String> getImageUrl() {
        return Optional.ofNullable(imageUrl);
    }
}
