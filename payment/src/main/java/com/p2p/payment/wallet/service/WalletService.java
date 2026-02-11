package com.p2p.payment.wallet.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p2p.payment.wallet.entity.Wallet;
import com.p2p.payment.wallet.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
	
	private final WalletRepository walletRepository;
	
	
	@Transactional(readOnly = true)
	public BigDecimal getBalance(UUID userId) {
		
		Wallet wallet = walletRepository.findByUserId(userId)
		.orElseThrow(() -> new IllegalStateException("Wallet not found"));
		
		return wallet.getBalance();
	}
	
	@Transactional
	public void credit(UUID userId, BigDecimal amount) {
		
		validateAmount(amount);
		
		Wallet wallet = walletRepository.findByUserId(userId)
		.orElseThrow(() -> new IllegalStateException("Wallet not found"));
		
		wallet.setBalance(wallet.getBalance().add(amount));
		
		walletRepository.save(wallet);
	}
	
	@Transactional
	public void debit(UUID userId, BigDecimal amount) {
		
		validateAmount(amount);
		
		Wallet wallet = walletRepository.findByUserId(userId)
		.orElseThrow(() -> new IllegalStateException("Wallet not found"));
		
		if (wallet.getBalance().compareTo(amount) < 0) {
			throw new IllegalStateException("Insufficient balance");
		}
		
		wallet.setBalance(wallet.getBalance().subtract(amount));
		
		walletRepository.save(wallet);
	}
	
	public void validateAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount must be greater then Zero");
		}
	}
	
}