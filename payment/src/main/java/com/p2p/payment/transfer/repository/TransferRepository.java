package com.p2p.payment.transfer.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.p2p.payment.transfer.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
	
}