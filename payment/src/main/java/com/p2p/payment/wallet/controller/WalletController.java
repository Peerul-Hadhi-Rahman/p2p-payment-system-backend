package com.p2p.payment.wallet.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.p2p.payment.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
	
	private final WalletService walletService;
	
	@GetMapping("/balance")
	public BigDecimal getBalance(Authentication authentication) {
		
		UUID userId = UUID.fromString(authentication.getName());
		
		return walletService.getBalance(userId);
	}
	
	@PostMapping("/credit")
	public void credit(@RequestParam BigDecimal amount, 
			Authentication authentication) {
		
		UUID userId = extractUserId(authentication);
		walletService.credit(userId, amount);
	}
	
	@PostMapping("/debit")
	public void debit(@RequestParam BigDecimal amount, 
			Authentication authentication) {
		
		UUID userId = extractUserId(authentication);
		walletService.debit(userId, amount);
	}
	
	private UUID extractUserId(Authentication authentication) {
		
		if(authentication == null || authentication.getName() == null) {
			throw new IllegalStateException("Unauthenticated request");
		}
		return UUID.fromString(authentication.getName());
	}
}