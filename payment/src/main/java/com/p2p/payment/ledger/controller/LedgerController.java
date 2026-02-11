package com.p2p.payment.ledger.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p2p.payment.ledger.entity.LedgerEntry;
import com.p2p.payment.ledger.repository.LedgerRepository;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {
	
	private final LedgerRepository ledgerRepository;
	
	
	
	@GetMapping
	public List<LedgerEntry> getMyLedger(Authentication authentication) {
		UUID userId = UUID.fromString(authentication.getName());
		return ledgerRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}
}
	

