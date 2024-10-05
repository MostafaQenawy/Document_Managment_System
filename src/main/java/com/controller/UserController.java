package com.controller;

import com.entity.Role;
import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.user}")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@Valid @PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/authority/{id}")
    public ResponseEntity<?> setAuthorities(  @PathVariable Long id ,@RequestBody Set<Role> roles ){
        return ResponseEntity.ok(userService.setAuthorities(id , roles));
    }
}
