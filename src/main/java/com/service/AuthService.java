package com.service;
import com.Advice.BaseApiExcepetions;
import com.security.JWTResponseDto;
import com.security.JwtRequestDto;
import com.security.JwtTokenUtils;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.User;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	public JWTResponseDto login(JwtRequestDto jwtRequestDto) {
		User user = userService.loadUserByEmail(jwtRequestDto.getEmail());

		if (!passwordEncoder.matches(jwtRequestDto.getPassword(), user.getPassword())) {
			throw new BaseApiExcepetions(String.format("Wrong password has been invoken"), HttpStatus.BAD_REQUEST);
		}
		String jwtToken = jwtTokenUtils.generateToken(user.getEmail() , user.getId());

		return new JWTResponseDto(jwtToken);

	}

}
