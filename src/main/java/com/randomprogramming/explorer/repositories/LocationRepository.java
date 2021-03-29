package com.randomprogramming.explorer.repositories;

import com.randomprogramming.explorer.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findFirstById(String id);

    Page<Location> findAllByTitleLike(String title, Pageable pageable);

    Page<Location> findAllByLatitudeBetweenAndLongitudeBetweenOrderByLikeCount(
            double latitudeMin, double latitudeMax, double longitudeMin, double longitudeMax, Pageable pageable);
}
