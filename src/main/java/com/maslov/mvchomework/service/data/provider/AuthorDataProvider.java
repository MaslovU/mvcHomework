package com.maslov.mvchomework.service.data.provider;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.repository.AuthorRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorDataProvider {

    private final AuthorRepo authorRepo;

    public List<Author> findByName(String name) {
        return authorRepo.findByName(name);
    }

    public Author create(Author author) {
        return authorRepo.save(author);
    }
}
