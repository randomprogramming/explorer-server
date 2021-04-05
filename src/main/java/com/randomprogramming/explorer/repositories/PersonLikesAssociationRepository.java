package com.randomprogramming.explorer.repositories;

import com.randomprogramming.explorer.entities.PersonLikesAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PersonLikesAssociationRepository extends JpaRepository<PersonLikesAssociation, String> {
    boolean existsByPersonIdAndLocationId(String personId, String locationId);

    @Transactional
    void deleteByPersonIdAndLocationId(String personId, String locationId);
}
