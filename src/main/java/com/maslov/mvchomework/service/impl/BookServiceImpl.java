package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.domain.Genre;
import com.maslov.mvchomework.domain.YearOfPublish;
import com.maslov.mvchomework.exception.NoBookException;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.repository.AuthorRepo;
import com.maslov.mvchomework.repository.BookRepo;
import com.maslov.mvchomework.repository.CommentRepo;
import com.maslov.mvchomework.repository.GenreRepo;
import com.maslov.mvchomework.repository.YearRepo;
import com.maslov.mvchomework.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private final YearRepo yearRepo;
    private final GenreRepo genreRepo;
    private final CommentRepo commentRepo;
    private final AuthorRepo authorRepo;


//    public BookServiceImpl(BookRepo bookRepo, YearRepo yearRepo, GenreRepo genreRepo, CommentRepo commentRepo) {
//        this.bookRepo = bookRepo;
//        this.yearRepo = yearRepo;
//        this.genreRepo = genreRepo;
//        this.commentRepo = commentRepo;
//    }

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
        List<Author> authors = createListAuthors(bookModel);
        List<Comment> comments = createComments(bookModel);
        book.setName(bookModel.getName());
        book.setYear(savedYear);
        book.setGenre(savedGenre);
        book.setAuthors(authors);
        book.setListOfComments(comments);
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
        if (isNull(yearOfPublish)) {
            return yearRepo.save(YearOfPublish.builder().dateOfPublish(book.getYear()).build());
        }
        return yearOfPublish;
    }

    private Genre checkIfGenreIsExist(BookModel book) {
        Genre genre = genreRepo.findByName(book.getGenre());
        if (isNull(genre)) {
            return genreRepo.save(Genre.builder().name(book.getGenre()).build());
        }
        return genre;
    }

    private List<Comment> createComments(BookModel bookModel) {
        List<Comment> commentList = new ArrayList<>();
        for (String b : bookModel.getComments()) {
            commentList.add(commentRepo.save(Comment.builder().commentForBook(b).build()));
        }
        return commentList;
    }

    private List<Author> createListAuthors(BookModel bookModel) {
        List<Author> authorList = new ArrayList<>();
        for (String a: bookModel.getAuthors()) {
            Author author = checkIfAuthorIsExist(a);
            authorList.add(author);
        }
        return authorList;
    }

    private Author checkIfAuthorIsExist(String authorName) {
        try {
            return authorRepo.findByName(authorName).get(0);
        } catch (IndexOutOfBoundsException e) {
            return authorRepo.save(Author.builder().name(authorName).build());
        }
    }
}
