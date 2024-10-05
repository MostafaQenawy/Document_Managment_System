package com.security;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}