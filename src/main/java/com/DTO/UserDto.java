package com.DTO;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Column(nullable = false, length = 50)
    private String username;

    @Column( nullable = false , unique = true )
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @Column(unique = true, length = 14)
    private String nid;

    @Column(nullable = false )
    @Size(min = 8, max = 100)
    private String password;


}
