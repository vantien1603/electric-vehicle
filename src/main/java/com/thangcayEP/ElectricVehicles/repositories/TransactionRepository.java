package com.thangcayEP.ElectricVehicles.repositories;

import com.thangcayEP.ElectricVehicles.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
                SELECT n FROM Transaction n
                WHERE  n.buyer.id = :id
                AND (:status IS NULL OR n.status LIKE CONCAT('%', :status, '%'))
            """)
    Page<Transaction> findByBuyerId(@Param("id") Long id, @Param("status") String status, Pageable pageable);

    @Query("""
                SELECT n FROM Transaction n
                WHERE n.seller.id = :id
                AND (:status IS NULL OR n.status LIKE CONCAT('%', :status, '%'))
            """)
    Page<Transaction> findBySellerId(@Param("id") Long id, @Param("status") String status, Pageable pageable);


}
