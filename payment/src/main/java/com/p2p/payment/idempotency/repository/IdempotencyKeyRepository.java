package com.p2p.payment.idempotency.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.payment.idempotency.entity.IdempotencyKey;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {
	Optional<IdempotencyKey> findByKeyAndUserId(String key, UUID userId);
}