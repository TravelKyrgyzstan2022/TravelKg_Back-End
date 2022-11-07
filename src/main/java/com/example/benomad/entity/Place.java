package com.example.benomad.entity;


import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import lombok.*;

import javax.persistence.*;

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
    private PlaceCategory placeCategory;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, name = "image_id")
    private Long imageId;

    @Column(nullable = false, name = "link_url")
    private String linkUrl;

    @Column(nullable = true)
    private String address;

}