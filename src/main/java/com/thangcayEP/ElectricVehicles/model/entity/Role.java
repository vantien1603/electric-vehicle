package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;
}
