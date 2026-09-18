package com.transaction.core.repository;

import com.transaction.core.entity.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry,Long> {
    Page<LedgerEntry> findByAccountId(Long accountId, Pageable pageable);
}
