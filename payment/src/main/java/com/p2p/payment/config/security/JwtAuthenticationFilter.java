package com.p2p.payment.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.p2p.payment.auth.security.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.startsWith("/api/auth");
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String token = authHeader.substring(7);
	    String email;

	    try {
	        email = jwtService.extractClaims(token).get("email", String.class);
	    } catch (Exception e) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        try {
	            UserDetails userDetails =
	                    userDetailsService.loadUserByUsername(email);

	            String userId = jwtService.extractUserId(token).toString();

	            UsernamePasswordAuthenticationToken authentication =
	                    new UsernamePasswordAuthenticationToken(
	                            userId, null, userDetails.getAuthorities());

	            authentication.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request));

	            SecurityContextHolder.getContext().setAuthentication(authentication);

	        } catch (UsernameNotFoundException ex) {
	            // Token valid but user no longer exists → ignore
	        }
	    }

	    filterChain.doFilter(request, response);
	}
		
}