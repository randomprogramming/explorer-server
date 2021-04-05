package com.randomprogramming.explorer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @JsonIgnore
    @OneToMany(mappedBy = "likedBy")
    private Set<PersonLikesAssociation> likedLocationsAssociations;

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

    public List<Location> getLikedLocations() {
        return this.getLikedLocationsAssociations()
                .stream()
                .map(PersonLikesAssociation::getLikedLocation)
                .collect(Collectors.toList());
    }

    public List<Location> getLikedLocations(Comparator<PersonLikesAssociation> comparator) {
        return this.getLikedLocationsAssociations()
                .stream()
                .sorted(comparator)
                .map(PersonLikesAssociation::getLikedLocation)
                .collect(Collectors.toList());
    }
}
