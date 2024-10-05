package com.repository;

import com.entity.Role;
import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    User findByUsername(String username);
    User findByEmail(String email);
}
