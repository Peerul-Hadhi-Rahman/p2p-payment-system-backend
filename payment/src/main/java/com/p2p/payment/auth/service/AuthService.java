package com.p2p.payment.auth.service;

import java.math.BigDecimal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.p2p.payment.auth.dto.AuthResponse;
import com.p2p.payment.auth.dto.LoginRequest;
import com.p2p.payment.auth.dto.RegisterRequest;
import com.p2p.payment.config.security.JwtService;
import com.p2p.payment.user.entity.User;
import com.p2p.payment.user.repository.UserRepository;
import com.p2p.payment.wallet.entity.Wallet;
import com.p2p.payment.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final WalletRepository walletRepository;
	
	private final JwtService jwtService;
	
	
	public void register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalStateException("Email already registered");
		}
		
		User user = User.builder()
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.role(User.Role.USER)
				.status(User.Status.ACTIVE)
				.build();
		
		User savedUser = userRepository.save(user);
		
		walletRepository.save(
				 Wallet.builder()
				.user(savedUser)
				.balance(BigDecimal.ZERO)
				.build()
				);
	}
	
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.email(), request.password()
				)
				);
		
		User user = userRepository.findByEmail(request.email()).orElseThrow();
		
		String token = jwtService.generateToken(user.getId(), user.getEmail());
		return new AuthResponse(token);
				
	}
	
}