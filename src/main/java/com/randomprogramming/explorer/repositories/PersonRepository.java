package com.randomprogramming.explorer.repositories;

import com.randomprogramming.explorer.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    Person findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
