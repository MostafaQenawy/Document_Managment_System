package com.controller;

import com.DTO.UserDto;
import com.entity.User;
import com.service.AuthService;
import com.security.JWTResponseDto;
import com.security.JwtRequestDto;
import com.security.JwtTokenUtils;
import com.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> login (@Valid @RequestBody JwtRequestDto jwtRequest){
        return ResponseEntity.ok(authService.login(jwtRequest));
    }

    @PostMapping("/register")
    public Map register (@Valid @RequestBody UserDto userDto){

        return userService.save(userDto);
    }
}
