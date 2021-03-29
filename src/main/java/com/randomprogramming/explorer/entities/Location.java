package com.randomprogramming.explorer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

// Lombok hashcode breaks everything, do not use
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Location {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String title;

    @Formula("(select count(*) from person_liked_locations pll where pll.location_id=id)")
    private int likeCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Media> media;

    @ManyToMany(mappedBy = "likedLocations")
    @JsonIgnore
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
