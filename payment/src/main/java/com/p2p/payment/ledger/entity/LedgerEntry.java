package com.p2p.payment.ledger.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "ledger_entries", indexes = {
		@Index(name = "idx_ledger_user", columnList = "userId"),
		@Index(name = "idx_ledger_ref", columnList = "referenceId")
}) 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(nullable = false)
	private  UUID userId;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EntryType type;
	
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	@Column(name = "reference_id", nullable = false)
	private UUID referenceId;
	
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	
	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
	}
	
	public enum EntryType {
		CREDIT,
		DEBIT
	}
	
	public enum TransactionStatus {
		SUCCESS,
		FAILED
	}
}