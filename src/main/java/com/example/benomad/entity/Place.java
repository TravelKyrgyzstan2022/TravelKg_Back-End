package com.example.benomad.entity;


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
    @ManyToOne()
    @JoinColumn(name = "region_id",nullable = false,referencedColumnName = "id")
    private Region region;
    @ManyToOne()
    @JoinColumn(name = "place_type_id",referencedColumnName = "id")
    private PlaceType placeType;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false,name = "image_url")
    private String imageUrl;
    @Column(nullable = false,name = "link_url")
    private String linkUrl;
    @Column(nullable = true)
    private String address;

}
