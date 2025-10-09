package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private String status = "PENDING"; // APPROVED, SOLD, HIDDEN, DELETED

    private String vehicleType;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private Long vehicleMileage;
    private String vehicleBatteryCapacity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NewsImage> images;

}
