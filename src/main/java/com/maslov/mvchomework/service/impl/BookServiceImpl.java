package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.domain.Genre;
import com.maslov.mvchomework.domain.YearOfPublish;
import com.maslov.mvchomework.exception.NoBookException;
import com.maslov.mvchomework.model.AuthorModel;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.model.CommentModel;
import com.maslov.mvchomework.repository.BookRepo;
import com.maslov.mvchomework.repository.GenreRepo;
import com.maslov.mvchomework.repository.YearRepo;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.ScannerHelper;
import liquibase.pro.packaged.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private static final String ENTER_ID = "Enter ID for book or 0 is your dont now ID";
    private static final String GET_ALL = "Enter command 'getall' for search your book in list";

    private final BookRepo bookRepo;
    private final ScannerHelper helper;
    private final YearRepo yearRepo;
    private final GenreRepo genreRepo;


    public BookServiceImpl(BookRepo bookRepo, ScannerHelper helper, YearRepo yearRepo, GenreRepo genreRepo) {
        this.bookRepo = bookRepo;
        this.helper = helper;
        this.yearRepo = yearRepo;
        this.genreRepo = genreRepo;
    }

    @Override
    public BookModel getBook(long id) {
        try {
            Book book = bookRepo.findById(id).orElseThrow();
            return toBookModel(book);
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
    public List<Book> createBook(BookModel bookModel) {
        log.debug("Start creating book");
        Book book = new Book();
        YearOfPublish savedYear = checkIfYearIsExist(bookModel);
        Genre savedGenre = checkIfGenreIsExist(bookModel);
        book.setName(book.getName());
        book.setYear(savedYear);
        book.setGenre(savedGenre);
        bookRepo.save(book);
        return bookRepo.findAll();
    }

    @Override
    @Transactional
    public BookModel updateBook(BookModel book, Book bookFromDB) {
        log.debug("Start updating book");
        BeanUtils.copyProperties(book, bookFromDB, "id");
        return toBookModel(bookRepo.save(bookFromDB));
    }

    @Override
    public void delBook(long id) {
        bookRepo.deleteById(id);
        log.info("Book deleted successfully");
    }

    private YearOfPublish checkIfYearIsExist(BookModel bookModel) {
        YearOfPublish savedYear = yearRepo.findByDateOfPublish(bookModel.getYear());
        if (savedYear.getDateOfPublish().isEmpty()) {
            return yearRepo.save(YearOfPublish.builder().dateOfPublish(bookModel.getYear()).build());
        }
        return savedYear;
    }

    private Genre checkIfGenreIsExist(BookModel bookModel) {
        Genre savedGenre = genreRepo.findByName(bookModel.getYear());
        if (savedGenre.getName().isEmpty()) {
            return genreRepo.save(Genre.builder().name(bookModel.getYear()).build());
        }
        return savedGenre;
    }

    private BookModel toBookModel(Book book) {
        List<CommentModel> comments = book.getListOfComments()
                .stream()
                .map(Comment::getCommentForBook)
                .map(c -> CommentModel.builder().commentForBook(c).build())
                .collect(Collectors.toList());
        List<AuthorModel> authors = book.getAuthor()
                .stream()
                .map(Author::getName)
                .map(a -> AuthorModel.builder().name(a).build())
                .collect(Collectors.toList());
        return BookModel.builder()
                .name(book.getName())
                .authors(authors)
                .genre(book.getGenre().getName())
                .year(book.getYear().getDateOfPublish())
                .comments(comments)
                .build();
    }
}
