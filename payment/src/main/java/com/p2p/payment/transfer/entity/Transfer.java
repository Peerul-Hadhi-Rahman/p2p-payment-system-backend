package com.p2p.payment.transfer.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(nullable = false)
	private UUID senderUserId;
	
	@Column(nullable = false)
	private UUID receiverUserId;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TransferStatus status;
	
	@Column(name = "reference_id", nullable = false, updatable = false)
	private UUID referenceId;
	
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	
	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
	}
}