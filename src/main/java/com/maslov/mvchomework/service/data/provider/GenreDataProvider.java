package com.maslov.mvchomework.service.data.provider;

import com.maslov.mvchomework.domain.Genre;
import com.maslov.mvchomework.repository.GenreRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreDataProvider {

    private final GenreRepo genreRepo;

    public Genre findByName(String name) {
        return genreRepo.findByName(name);
    }

    public Genre createGenre(Genre genre) {
        return genreRepo.save(genre);
    }
}
