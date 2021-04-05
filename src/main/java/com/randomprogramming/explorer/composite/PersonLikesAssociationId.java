package com.randomprogramming.explorer.composite;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class PersonLikesAssociationId implements Serializable {
    private String personId;

    private String locationId;

    @Override
    public int hashCode() {
        return Integer.parseInt(personId) + Integer.parseInt(locationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PersonLikesAssociationId) {
            var otherId = (PersonLikesAssociationId) obj;
            return (otherId.personId.equals(this.personId)) && (otherId.locationId.equals(this.locationId));
        }
        return false;
    }
}
