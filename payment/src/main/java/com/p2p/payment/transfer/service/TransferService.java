package com.p2p.payment.transfer.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.p2p.payment.idempotency.service.IdempotencyService;
import com.p2p.payment.ledger.entity.LedgerEntry;
import com.p2p.payment.ledger.service.LedgerService;
import com.p2p.payment.transfer.entity.Transfer;
import com.p2p.payment.transfer.entity.TransferStatus;
import com.p2p.payment.transfer.repository.TransferRepository;
import com.p2p.payment.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class TransferService {
	
	private final WalletService walletService;
																																																																									
	private final TransferRepository transferRepository;
	
	private final LedgerService ledgerService;
	
	private final IdempotencyService idempotencyService;
	


	@Transactional
	public UUID transfer(UUID senderUserId,
			UUID receiverUserId, BigDecimal amount, String idempotencyKey
			) {
		
		if(senderUserId.equals(receiverUserId)) {
			throw new IllegalArgumentException("Cannot transfer to self");
		}
		
		if(idempotencyKey != null && ! idempotencyKey.isBlank()) {
			Optional<UUID> existing = idempotencyService.findExisting(idempotencyKey, senderUserId);
			
			if(existing.isPresent()) {
				return existing.get();
			}
		}
		
		UUID refId = UUID.randomUUID();
		
		Transfer transfer = transferRepository.save(
				Transfer.builder()
				.senderUserId(senderUserId)
				.receiverUserId(receiverUserId)
				.amount(amount)
				.status(TransferStatus.INITIATED)
				.referenceId(refId)
				.build()
				);
					
		try {
		walletService.debit(senderUserId, amount);
		ledgerService.record(senderUserId, amount.negate(), LedgerEntry.EntryType.DEBIT, refId);
		
		walletService.credit(receiverUserId, amount);
		ledgerService.record(receiverUserId, amount, LedgerEntry.EntryType.CREDIT, refId);
		
		
		transfer.setStatus(TransferStatus.SUCCESS);
		transferRepository.save(transfer);

		if(idempotencyKey != null && ! idempotencyKey.isBlank()) {
			idempotencyService.save(idempotencyKey, senderUserId, refId);
		}
		return refId;
		} 
		catch (Exception ex) {
			transfer.setStatus(TransferStatus.FAILED);
			transferRepository.save(transfer);
			throw ex;
		}
	}  
}