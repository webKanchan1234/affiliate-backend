package com.blogdirectorio.affiliate.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.blogdirectorio.affiliate.entity.UserEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenHelper {

	private static final long JWT_TOKEN_VALIDITY = 2 * 24 * 60 * 60 * 1000;

	private static final String SECRET_KEY = "mySuperSecretKeyForJWTEncryption";

	private final SecretKey secretKey = new SecretKeySpec(Base64.getEncoder().encode(SECRET_KEY.getBytes()),
			"HmacSHA512");

	@Autowired
	private UserRepository userRepo;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(UserDetails userDetails, Long userId) {

		UserEntity user = userRepo.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("user ", "id", null));

		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId.toString());
		claims.put("roles", userDetails.getAuthorities());
		String token = doGenerateToken(claims, userDetails.getUsername());

		user.setJwtToken(token); // Store token in database
		userRepo.save(user);
		return token;
	}

	private String doGenerateToken(Map<String, Object> claims, String userName) {
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Token valid for 30 minutes
				.signWith(secretKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {

		final String username = getUsernameFromToken(token);
		UserEntity user = userRepo.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", null));
		;

		if (user == null || !token.equals(user.getJwtToken())) {
//            System.out.println("Invalid or expired token!");
			return false;
		}

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Long extractUserId(String token) {
		Object userId = getAllClaimsFromToken(token).get("userId");
//	System.out.println("helper =="+userId);
		return userId instanceof Integer ? ((Integer) userId).longValue() : Long.valueOf(userId.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRolesFromToken(String token) {
	    Claims claims = getAllClaimsFromToken(token);
	    return (List<String>) claims.get("roles");
	}

}
