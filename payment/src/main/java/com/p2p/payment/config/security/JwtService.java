package com.p2p.payment.config.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "LIRpOsZzhd2sxiEJJiCa8ekJQ1Yz1tpw";
	private static final long EXPIRATION_TIME = 1000*60*60*24;
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	public String generateToken(UUID userId, String email) {
		return Jwts.builder()
				.subject(userId.toString())
				.claim("email", email)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSigningKey())
				.compact();
	}
	
	public Claims extractClaims(String token) {
	    return Jwts.parser()
	            .verifyWith((javax.crypto.SecretKey) getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	}
	
	public UUID extractUserId(String token) {
		return UUID.fromString(extractClaims(token).getSubject());
	}
	
}