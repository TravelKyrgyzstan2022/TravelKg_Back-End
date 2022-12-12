package com.example.benomad.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(columnDefinition = "user_id",
                referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(columnDefinition = "place_id",
            referencedColumnName = "id")
    private Place place;

    @Column(name = "date")
    private LocalDate date;

    @Column(length = 500)
    private String note;
}
