package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.domain.Genre;
import com.maslov.mvchomework.domain.YearOfPublish;
import com.maslov.mvchomework.exception.NoBookException;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.repository.BookRepo;
import com.maslov.mvchomework.repository.GenreRepo;
import com.maslov.mvchomework.repository.YearRepo;
import com.maslov.mvchomework.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private final YearRepo yearRepo;
    private final GenreRepo genreRepo;


    public BookServiceImpl(BookRepo bookRepo, YearRepo yearRepo, GenreRepo genreRepo) {
        this.bookRepo = bookRepo;
        this.yearRepo = yearRepo;
        this.genreRepo = genreRepo;
    }

    @Override
    public Book getBook(long id) {
        try {
            return bookRepo.findById(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException e) {
            throw new NoBookException("Book with this id is not exist");
        }
    }

    @Override
    public List<Book> getAllBook() {
        return bookRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments(long id) {
        return bookRepo.findById(id).orElseThrow().getListOfComments();
    }

    @Override
    @Transactional
    public Book createBook(BookModel bookModel) {
        log.debug("Start creating book");
        Book book = new Book();
        YearOfPublish savedYear = checkIfYearIsExist(bookModel);
        Genre savedGenre = checkIfGenreIsExist(bookModel);
        book.setName(book.getName());
        book.setYear(savedYear);
        book.setGenre(savedGenre);
        return bookRepo.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(BookModel bookModel, Book bookFromDB) {
        log.debug("Start updating book");
        BeanUtils.copyProperties(bookModel, bookFromDB, "id");
        return bookRepo.save(bookFromDB);
    }

    @Override
    public void delBook(long id) {
        bookRepo.deleteById(id);
        log.info("Book deleted successfully");
    }

    private YearOfPublish checkIfYearIsExist(BookModel book) {
        YearOfPublish yearOfPublish = yearRepo.findByDateOfPublish(book.getYear());
        if (yearOfPublish.getDateOfPublish().isEmpty()) {
            return yearRepo.save(YearOfPublish.builder().dateOfPublish(book.getYear()).build());
        }
        return yearOfPublish;
    }

    private Genre checkIfGenreIsExist(BookModel book) {
        Genre genre = genreRepo.findByName(book.getGenre());
        if (genre.getName().isEmpty()) {
            return genreRepo.save(Genre.builder().name(book.getGenre()).build());
        }
        return genre;
    }
}
