package com.service;

import com.Advice.BaseApiExcepetions;
import com.DTO.UserDto;
import com.entity.Role;
import com.entity.User;
import com.mapper.UserMapper;
import com.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserMapper userMapper;
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public UserDto findById(Long id){

        User user = userRepo.findById(id).orElseThrow(() -> new BaseApiExcepetions(String.format("No Record with user_id [%d] found in data base " , id) , HttpStatus.NOT_FOUND));
        UserDto userDto = userMapper.Map(user);
        return userDto;
    }

    public Map save(UserDto userDto) {
            User user = userMapper.UnMap(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            Map<String ,String> map = new HashMap<>();
            map.put("status" , "success");
            map.put("message" ,"User had been added successfully!");
            return map;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null)
            throw new BaseApiExcepetions(String.format("No Record with user_name [%s] found in database " , username), HttpStatus.NOT_FOUND);
        return user;
    }

    private static List<GrantedAuthority> getAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(!user.getRoles().isEmpty())
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        return authorities;

    }
    public UserDto setAuthorities(Long id , Set<Role> roles)
    {
        System.out.println("start Authorization");
        User user = userRepo.findById(id).orElseThrow(() -> new BaseApiExcepetions(String.format("No Record with user_id [%d] found in data base " , id) , HttpStatus.NOT_FOUND));
        user.setRoles(roles);
        user = userRepo.save(user);
        System.out.println("finish Authorization");
        return userMapper.Map(user);
    }

    public User loadUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if(user == null)
            throw new BaseApiExcepetions(String.format("This Email not registered yet " , email), HttpStatus.NOT_FOUND);
        return user;
    }
}
