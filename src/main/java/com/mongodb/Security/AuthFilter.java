package com.mongodb.Security;

import java.io.IOException;

import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils tokenUtil;

    @Autowired
    public AuthFilter(JwtTokenUtils tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            log.info("Path is >> " + request.getRequestURL());
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());

            tokenUtil.setJwtToken(jwtTokenHeader.substring("Bearer ".length()));

            String email = tokenUtil.getEmailFromToken(jwtToken);
            if (email != null && tokenUtil.validateToken(jwtToken)){
                log.info("validateToken(-)");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        null, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
