package com.randomprogramming.explorer.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Media> media;

    @ManyToMany(mappedBy = "likedLocations")
    private Set<Person> likedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    private Person createdBy;

    public Location(double latitude, double longitude, String title, Set<Media> media, Person createdBy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.media = media;
        this.createdBy = createdBy;
    }
}
