package com.transaction.core.repository;

import com.transaction.core.constants.EntryType;
import com.transaction.core.entity.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LedgerEntryRepository
        extends JpaRepository<LedgerEntry,Long> , JpaSpecificationExecutor<LedgerEntry> {

    @EntityGraph(attributePaths = "transaction")
    Page<LedgerEntry> findByAccountId(Long accountId, Pageable pageable);

    @Query("""
            SELECT MAX(l.amount) FROM LedgerEntry l WHERE l.account.id = :accountId AND l.entryType = :entryType
            AND l.amount < (SELECT MAX(l2.amount) FROM LedgerEntry l2 WHERE l2.account.id = :accountId AND l2.entryType = :entryType)
            """)
    BigDecimal findByAccountIdAndEntryType(@Param("accountId") Long accountId, @Param("entryType") EntryType entryType);

}
