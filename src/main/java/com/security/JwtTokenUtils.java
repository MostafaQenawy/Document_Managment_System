package com.security;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.entity.User;
import com.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
@Slf4j
public class JwtTokenUtils {

	@Value("${auth.secret}")
	private String TOKEN_SECRET;
	@Value("${auth.access.expiration}")
	private Long ACCESS_TOKEN_VALIDITY;

	public JwtTokenUtils(){

	}
	public JwtTokenUtils(String secret,  Long accessValidity) {
		Assert.notNull(accessValidity, "Validity must not be null");
		Assert.hasText(secret, "Validity must not be null or empty");

		TOKEN_SECRET = secret;
		ACCESS_TOKEN_VALIDITY = accessValidity;
	}

	public String generateToken(String Email, Long userId) {
		//TODO:Add userid in token
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(Email)
				.setIssuedAt(new Date())
				.setIssuer("app-Service")
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY ))
				.signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
	}


	public String getEmailFromToken(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject();
	}

	public boolean isTokenExpired(String token) {
		Date expiration = getClaims(token).getExpiration();
		return expiration.before(new Date());
	}

	private Claims getClaims(String token) {

         return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
	}


	public boolean validateToken(String token ){

        return !isTokenExpired(token);
}

}
