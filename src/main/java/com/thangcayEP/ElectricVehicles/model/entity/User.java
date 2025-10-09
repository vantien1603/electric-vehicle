package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String avatarUrl;

    private String address;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String status = "VERIFYING";

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = true)
    private String verificationCode;

    @Column(nullable = true)
    private String resetPasswordToken;

    @Column(nullable = true)
    private LocalDateTime resetPasswordExpiry;

    @Column(nullable = true)
    private LocalDateTime verificationCodeExpiry;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "user")
    private List<AccessToken> accessToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notifications> notifications;


}
