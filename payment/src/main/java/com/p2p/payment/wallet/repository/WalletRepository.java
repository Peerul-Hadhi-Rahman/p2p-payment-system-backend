package com.p2p.payment.wallet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.p2p.payment.wallet.entity.Wallet;


public interface WalletRepository extends JpaRepository<Wallet, UUID> {
	@Query("SELECT w FROM Wallet w WHERE w.user.id = :userId")
	Optional<Wallet> findByUserId(UUID userId);
}