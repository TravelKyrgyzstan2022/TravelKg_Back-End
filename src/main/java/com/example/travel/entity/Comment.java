package com.example.travel.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "place_id",nullable = false,referencedColumnName = "id")
    private Place place;
    @ManyToOne()
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "id")
    private User user;
    @Column(nullable = false,length = 2000)
    private String body;

}
