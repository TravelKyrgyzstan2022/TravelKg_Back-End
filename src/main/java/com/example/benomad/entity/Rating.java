package com.example.benomad.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "place_ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = "place_id",
            referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id",
            referencedColumnName = "id")
    private User user;

    private Integer rating;
}
