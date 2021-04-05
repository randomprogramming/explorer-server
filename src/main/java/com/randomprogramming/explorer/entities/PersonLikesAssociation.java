package com.randomprogramming.explorer.entities;

import com.randomprogramming.explorer.composite.PersonLikesAssociationId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "person_liked_locations")
@IdClass(PersonLikesAssociationId.class)
public class PersonLikesAssociation {
    @Id
    private String personId;

    @Id
    private String locationId;

    @PrimaryKeyJoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne
    private Person likedBy;

    @PrimaryKeyJoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne
    private Location likedLocation;

    @CreationTimestamp
    private Timestamp likeDate;

    public PersonLikesAssociation(Person person, Location location) {
        this.likedBy = person;
        this.personId = person.getId();

        this.likedLocation = location;
        this.locationId = location.getId();
    }
}
