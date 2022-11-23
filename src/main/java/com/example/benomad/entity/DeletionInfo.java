package com.example.benomad.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "deletion_info")
public class DeletionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    @Column(name = "deletion_date")
    private LocalDate deletionDate;

    @ManyToOne
    @JoinColumn(
            columnDefinition = "responsible_user_id",
            referencedColumnName = "id"
    )
    private User responsibleUser;
}
