package com.randomprogramming.explorer.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private boolean isEnabled;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}
