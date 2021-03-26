package com.randomprogramming.explorer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

// Lombok hashcode breaks everything, do not use
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Person {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;

    @JsonIgnore
    private boolean isEnabled;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String profilePictureUrl;

    @ManyToMany
    @JoinTable(
            name = "person_liked_locations",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @JsonIgnore
    private Set<Location> likedLocations;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private Set<Location> addedLocations;

    public Person(boolean isEnabled, String email, String username, String password, String profilePictureUrl) {
        this.isEnabled = isEnabled;
        this.email = email;
        this.username = username;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
    }
}
