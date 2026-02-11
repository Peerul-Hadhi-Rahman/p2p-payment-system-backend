package com.p2p.payment.transfer.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.p2p.payment.transfer.service.TransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transfer")
@RequiredArgsConstructor
public class TransferController {
	
	private final TransferService transferService;
	
	@PostMapping
	public UUID transfer(@RequestParam UUID receiverUserId, @RequestParam BigDecimal amount, 
			@RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
			Authentication authentication) {
		
		UUID senderUserId = UUID.fromString(authentication.getName());
		
		return transferService.transfer(senderUserId, receiverUserId, amount, idempotencyKey);
	}

}