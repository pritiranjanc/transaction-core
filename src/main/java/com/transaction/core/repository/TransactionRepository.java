package com.transaction.core.repository;

import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransferType;
import com.transaction.core.entity.Transaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT t.id
        FROM Transaction t
        WHERE t.status = :status
        AND t.transferType = :type
        ORDER BY t.id
    """)
    List<Long> findIdByStatusAndTransferTypeOrderById(@Param("status") TransactionStatus status, @Param("type") TransferType type,
                                                      Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.id = :id
        """)
    Optional<Transaction> findByIdForUpdate(@Param("id") Long id);
}
