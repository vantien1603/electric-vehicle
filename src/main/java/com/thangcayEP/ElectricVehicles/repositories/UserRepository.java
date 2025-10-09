package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

//    @Query(value = "SELECT * FROM users u WHERE u.is_delete = false and u.user_id = :userId", nativeQuery = true)
//    User findExistUserById(Long userId);

//    @Query("SELECT us FROM User us WHERE us.isDelete = false")
//    Page<User> findAllNotDeleted(Pageable pageable);

//    Page<User> findByRoleAndIsDeleteFalse(Role role, Pageable pageable);

    Optional<User> findById(long id);

    Optional<User> findByResetPasswordToken(String token);

    Optional<User> findByResetPasswordTokenAndEmail(String token, String email);
}
