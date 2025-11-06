package com.thangcayEP.ElectricVehicles.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(optional = false)

//    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Categories category;

    private BigDecimal price;

    private String status = "PENDING"; // APPROVED, SOLD, HIDDEN, DELETED
    private String reason;

    private String contactPhone;


    private String vehicleStatus;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private String color;
    private String vehicleBatteryCapacity;
    private Long topSpeed;
    private Long distanceTraveled;
    private String location;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NewsImage> images;

}
