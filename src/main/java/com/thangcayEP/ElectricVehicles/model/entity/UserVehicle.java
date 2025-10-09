//package com.thangcayEP.ElectricVehicles.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "user_vehicles")
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserVehicle {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
//    private User owner;
//
//    private String type;
//    private String brand;
//    private String model;
//    private Integer year;
//    private Long mileage;
//    private String batteryCapacity;
//    private String status;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private VehicileCategories category;
//
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//}
