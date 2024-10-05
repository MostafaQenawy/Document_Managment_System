package com.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id ;

    @Column
    private String name;

}
