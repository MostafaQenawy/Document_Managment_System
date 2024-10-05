package com.entity;


import jakarta.persistence.*;

import java.util.*;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id ;

    @Column(nullable = false, length = 50)
    private String username;

    @Column( nullable = false , unique = true )
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @Column(unique = true, length = 14)
    private String nid;

    @Column(nullable = false )
    @Size(min = 8 , message = "password minLength is 8")
    private String password;

    @Transient
    private List<GrantedAuthority> authorities = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role"
             , joinColumns = @JoinColumn(name = "user_id")
             , inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OrderColumn(name = "id")
    private Set<Role> roles = new HashSet<>();


    public User(User user) {
        super();
        this.id= user.getId();
        this.username= user.getUsername();
        this.email = user.getEmail();
        this.nid = user.getNid();
        this.password= user.getPassword();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }

    }

    public User(long id ,String username ,String email , String nid, String password , Set<Role> roles) {
        super();
        this.id= id;
        this.username= username;
        this.password= password;
        this.email = email;
        this.nid = nid;
        this.roles = roles;

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(!roles.isEmpty()) {
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
