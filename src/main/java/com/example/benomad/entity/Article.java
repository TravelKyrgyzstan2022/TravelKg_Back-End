package com.example.benomad.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String title;

    private String body;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(
            columnDefinition = "user_id",
            referencedColumnName = "id"
    )
    private User user;
}