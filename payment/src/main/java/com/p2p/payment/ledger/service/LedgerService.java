package com.p2p.payment.ledger.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.p2p.payment.ledger.entity.LedgerEntry;
import com.p2p.payment.ledger.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {
	
	private final LedgerRepository ledgerRepository;
	
	
	
	public void record(
			UUID userId,
			BigDecimal amount,
			LedgerEntry.EntryType type,
			UUID referenceId
			) {

		
		LedgerEntry entry = LedgerEntry.builder()
				.userId(userId)
				.amount(amount)
				.type(type)
				.status(LedgerEntry.TransactionStatus.SUCCESS)
				.referenceId(referenceId)
				.build();
		ledgerRepository.save(entry);
	}
}