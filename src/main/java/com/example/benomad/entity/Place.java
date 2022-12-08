package com.example.benomad.entity;


import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "image_urls")
    @ElementCollection(targetClass=String.class)
    private List<String> imageUrls = new ArrayList<>();

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
    
     @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;


}
