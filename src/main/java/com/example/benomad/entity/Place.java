package com.example.benomad.entity;


import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "place_category")
    private PlaceCategory placeCategory;

    @Column(nullable = false, length = 2000)
    private String description;

    @Getter(AccessLevel.NONE)
    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "link_url")
    private String linkUrl;

    @Column(nullable = true)
    private String address;

    @OneToMany
    @JoinTable(name = "place_comments",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments;

    public Optional<String> getImageUrl() {
        return Optional.ofNullable(imageUrl);
    }
}