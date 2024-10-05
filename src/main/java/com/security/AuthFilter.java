package com.security;

import java.io.IOException;

import com.entity.User;
import com.sun.net.httpserver.Filter;
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

import com.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtil;

    @Autowired
    public AuthFilter(JwtTokenUtils tokenUtil, UserService userService) {
        this.tokenUtil = tokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("Path is >> " + request.getRequestURL());
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());
            String Email = tokenUtil.getEmailFromToken(jwtToken);
            if (Email != null && tokenUtil.validateToken(jwtToken)) {
                log.info("validateToken(-)");
                User user =  userService.loadUserByEmail(Email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
