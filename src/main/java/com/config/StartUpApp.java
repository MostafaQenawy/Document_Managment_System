package com.config;

import com.DTO.UserDto;
import com.entity.Role;
import com.entity.User;
import com.service.RoleService;
import com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args)throws Exception{

        if (roleService.findAll().isEmpty()){
            roleService.save(new Role( 1 ,"admin"));
            roleService.save(new Role( 2 ,"user"));
            roleService.save(new Role( 3 ,"employee"));
        }

        if (userService.findAll().isEmpty()){
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleService.findByName("admin"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleService.findByName("user"));

            Set<Role> empRoles = new HashSet<>();
            empRoles.add(roleService.findByName("employee"));

            userService.save(new UserDto( "Mostafa","Mos@atos.net","1234-2345-4527" , "12345678"  ));
//            userService.save(new UserDto( "Mohamed","Moh@atos.net","5753-7895-4252" , "12348765" ));
//            userService.save(new UserDto( "Ahmed","Ahm@atos.net","3795-2576-2321" , "12348765" ));

        }
    }
}
