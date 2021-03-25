package com.randomprogramming.explorer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    private boolean isEnabled;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "person_liked_locations",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> likedLocations;

    @OneToMany(mappedBy = "createdBy")
    private Set<Location> addedLocations;

    public Person(boolean isEnabled, String email, String username, String password) {
        this.isEnabled = isEnabled;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
