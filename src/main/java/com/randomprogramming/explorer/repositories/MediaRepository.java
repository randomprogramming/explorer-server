package com.randomprogramming.explorer.repositories;

import com.randomprogramming.explorer.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String> {

}
