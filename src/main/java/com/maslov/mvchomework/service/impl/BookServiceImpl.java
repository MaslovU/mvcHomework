package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.domain.Genre;
import com.maslov.mvchomework.domain.YearOfPublish;
import com.maslov.mvchomework.exception.MaslovBookException;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.data.provider.AuthorDataProvider;
import com.maslov.mvchomework.service.data.provider.BookDataProvider;
import com.maslov.mvchomework.service.data.provider.CommentDataProvider;
import com.maslov.mvchomework.service.data.provider.GenreDataProvider;
import com.maslov.mvchomework.service.data.provider.YearDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDataProvider bookDataProvider;
    private final YearDataProvider yearDataProvider;
    private final GenreDataProvider genreDataProvider;
    private final CommentDataProvider commentDataProvider;
    private final AuthorDataProvider authorDataProvider;

    @Override
    public Book getBook(long id) {
        try {
            return bookDataProvider.getBook(id);
        } catch (NullPointerException e) {
            throw new MaslovBookException("Book with this id is not exist");
        }
    }

    @Override
    public List<Book> getAllBook() {
        return bookDataProvider.getAllBook();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments(long id) {
        return bookDataProvider.getBook(id).getListOfComments();
    }

    @Override
    @Transactional
    public Book createBook(BookModel bookModel) {
        log.debug("Start creating book");
        Book book = new Book();
        return makeFromModelToBook(bookModel, book);
    }

    @Override
    @Transactional
    public Book updateBook(BookModel bookModel, Book bookFromDB) {
        log.debug("Start updating book");
        return makeFromModelToBook(bookModel, bookFromDB);
    }

    @Override
    public void delBook(long id) {
        bookDataProvider.deleteById(id);
        log.info("Book deleted successfully");
    }

    private YearOfPublish checkIfYearIsExist(BookModel book) {
        YearOfPublish yearOfPublish = yearDataProvider.findByDate(book.getYear());
        if (isNull(yearOfPublish)) {
            return yearDataProvider.create(YearOfPublish.builder().dateOfPublish(book.getYear()).build());
        }
        return yearOfPublish;
    }

    private Genre checkIfGenreIsExist(BookModel book) {
        Genre genre = genreDataProvider.findByName(book.getGenre());
        if (isNull(genre)) {
            return genreDataProvider.createGenre(Genre.builder().name(book.getGenre()).build());
        }
        return genre;
    }

    private List<Comment> createComments(BookModel bookModel) {
        List<Comment> commentList = new ArrayList<>();
        for (String b : bookModel.getListOfComments()) {
            commentList.add(commentDataProvider.create(Comment.builder().commentForBook(b).build()));
        }
        return commentList;
    }

    private List<Author> createListAuthors(BookModel bookModel) {
        List<Author> authorList = new ArrayList<>();
        for (String a : bookModel.getAuthors()) {
            Author author = checkIfAuthorIsExist(a);
            authorList.add(author);
        }
        return authorList;
    }

    private Author checkIfAuthorIsExist(String authorName) {
        try {
            return authorDataProvider.findByName(authorName).get(0);
        } catch (IndexOutOfBoundsException e) {
            return authorDataProvider.create(Author.builder().name(authorName).build());
        }
    }

    private Book makeFromModelToBook(BookModel bookModel, Book book) {
        List<Author> authors = createListAuthors(bookModel);
        List<Comment> comments = createComments(bookModel);
        YearOfPublish savedYear = checkIfYearIsExist(bookModel);
        Genre savedGenre = checkIfGenreIsExist(bookModel);
        book.setName(bookModel.getName());
        book.setGenre(savedGenre);
        book.setYear(savedYear);
        book.setAuthors(authors);
        book.setListOfComments(comments);
        return bookDataProvider.createBook(book);
    }
}
