package com.p2p.payment.idempotency.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "idempotency_keys",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"key", "user_id"})
		}
		)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(nullable = false)
	private String key;
	
	@Column(name = "user_id", nullable = false)
	private UUID userId;
	
	@Column(name = "reference_id", nullable = false)
	private UUID referenceId;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
}