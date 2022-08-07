package com.maslov.mvchomework.repository;

import com.maslov.mvchomework.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
