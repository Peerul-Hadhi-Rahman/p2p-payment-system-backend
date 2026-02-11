package com.p2p.payment.ledger.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.p2p.payment.ledger.entity.LedgerEntry;

public interface LedgerRepository extends JpaRepository<LedgerEntry, UUID> {
	List<LedgerEntry> findByUserIdOrderByCreatedAtDesc(UUID userId);
	
	@Query("""
			SELECT COALESCE(SUM(le.amount), 0)
			FROM LedgerEntry le
			WHERE le.userId = :userId
			""")
	Optional<BigDecimal> sumAmountByUserId(@Param("userId") UUID userId);
}