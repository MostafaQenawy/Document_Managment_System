package com.mongodb.Security;

import java.util.Date;

//TODO: habdle AuthenticationContext


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
@Slf4j
public class JwtTokenUtils {

    @Value("${auth.secret}")
    private String TOKEN_SECRET;
    @Value("${auth.access.expiration}")
    private Long ACCESS_TOKEN_VALIDITY;

    private String jwtToken;


    public JwtTokenUtils(){

    }
    public JwtTokenUtils(String secret,  Long accessValidity) {
        Assert.notNull(accessValidity, "Validity must not be null");
        Assert.hasText(secret, "Validity must not be null or empty");

        TOKEN_SECRET = secret;
        ACCESS_TOKEN_VALIDITY = accessValidity;
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId" , Long.class);
    }



    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token){
        return !isTokenExpired(token);
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
