package com.service;

import com.Advice.BaseApiExcepetions;
import com.DTO.RoleDto;
import com.entity.Role;
import com.mapper.RoleMapper;
import com.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    @Autowired
    private final RoleRepo roleRepo;
    @Autowired
    private final RoleMapper roleMapper;
    public List<Role> findAll() {
        return roleRepo.findAll();
    }
    public RoleDto findById(Long id){
        Role role = roleRepo.findById(id).orElseThrow(()-> new BaseApiExcepetions(String.format("No Record with role_id [%d] found in data base " , id) , HttpStatus.NOT_FOUND));
        RoleDto roleDto= roleMapper.Map(role);

        return roleDto;
    }

    public Role findByName(String name){
        Role role = roleRepo.findByName(name);
        if(role == null)
            throw new BaseApiExcepetions(String.format("No Record with role_name [%s] found in data base " , name),HttpStatus.NOT_FOUND);
        return role;
    }

    public Role save(Role entity) {
            return roleRepo.save(entity);
    }
}
