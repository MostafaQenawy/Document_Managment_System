package com.controller;

import com.entity.Role;
import com.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.role}")
@RequiredArgsConstructor
public class RoleController {

    private String getUserUrl;
    @Autowired
    private final RoleService roleService;
    @GetMapping("")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(roleService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@Valid @PathVariable Long id){

        return ResponseEntity.ok(roleService.findById(id));
    }
}
