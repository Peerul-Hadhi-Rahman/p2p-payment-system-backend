package com.p2p.payment.user.repository;

import com.p2p.payment.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}