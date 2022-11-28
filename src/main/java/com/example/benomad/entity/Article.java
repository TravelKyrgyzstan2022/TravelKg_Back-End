package com.example.benomad.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "image_urls")
    @ElementCollection(targetClass=String.class)
    private List<String> imageUrls = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            columnDefinition = "user_id",
            referencedColumnName = "id"
    )
    private User user;

}
