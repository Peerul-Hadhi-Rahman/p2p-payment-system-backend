package com.p2p.payment.idempotency.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.p2p.payment.idempotency.entity.IdempotencyKey;
import com.p2p.payment.idempotency.repository.IdempotencyKeyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
	
	private final IdempotencyKeyRepository repository;
	
	public Optional<UUID> findExisting(String key, UUID userId) {
		return repository.findByKeyAndUserId(key, userId)
				.map(IdempotencyKey::getReferenceId);
	}
	
	public void save(String key, UUID userId, UUID referenceId) {
		repository.save(IdempotencyKey.builder()
				.key(key)
				.userId(userId)
				.referenceId(referenceId)
				.createdAt(Instant.now())
				.build()
				);
	}
	
}