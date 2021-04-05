package com.randomprogramming.explorer.comparators;

import com.randomprogramming.explorer.entities.PersonLikesAssociation;

import java.util.Comparator;

public class SortPersonLikesAssociationByLikeDate implements Comparator<PersonLikesAssociation> {
    @Override
    public int compare(PersonLikesAssociation o1, PersonLikesAssociation o2) {
        // This sorts newest to oldest
        return o2.getLikeDate().compareTo(o1.getLikeDate());
    }
}
