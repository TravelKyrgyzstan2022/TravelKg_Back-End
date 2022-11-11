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

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id",
                referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(columnDefinition = "place_id",
            referencedColumnName = "id")
    private Place place;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 500)
    private String note;
}