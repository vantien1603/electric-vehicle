//package com.thangcayEP.ElectricVehicles.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Entity
//@Table(name = "vehicle_catetories")
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class VehicileCategories {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true, length = 100)
//    private String name;
//
//    private String description;
//
//    @OneToMany(mappedBy = "category")
//    private List<UserVehicle> vehicles;
//}
