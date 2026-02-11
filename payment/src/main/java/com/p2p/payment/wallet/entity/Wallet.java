package com.p2p.payment.wallet.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.p2p.payment.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "wallets",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "user_id")
		}
		)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false, precision =  19, scale = 4)
	private BigDecimal balance;
	
	@Version
	private Long version;
	
	@Column(nullable = false)
	private Instant createdAt;
	
	@Column(nullable = false)
	private Instant updatedAt;
	
	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
		updatedAt = Instant.now();
		if (balance == null) {
			balance = BigDecimal.ZERO;
		}
	}
	
	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}
}