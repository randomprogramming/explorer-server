package com.randomprogramming.explorer.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;


}
