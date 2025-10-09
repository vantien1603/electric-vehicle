package com.thangcayEP.ElectricVehicles.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private boolean expired;

    private boolean revoked;

    @OneToMany(mappedBy = "refreshToken", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccessToken> accessTokens;
}
