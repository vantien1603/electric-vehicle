package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    private double rating;
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
