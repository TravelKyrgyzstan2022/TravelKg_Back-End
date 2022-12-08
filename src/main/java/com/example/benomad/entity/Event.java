package com.example.benomad.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "image_urls")
    @ElementCollection(targetClass=String.class)
    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false, name = "link_url")
    private String linkUrl;

    private String address;
}