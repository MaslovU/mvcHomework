package com.maslov.mvchomework.repository;

import com.maslov.mvchomework.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepo extends JpaRepository<Author, Long> {
    List<Author> findByName(String text);
}
