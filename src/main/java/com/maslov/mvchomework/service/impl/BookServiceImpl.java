package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.exception.NoBookException;
import com.maslov.mvchomework.model.AuthorModel;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.model.CommentModel;
import com.maslov.mvchomework.repository.BookRepo;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.BookServiceHelper;
import com.maslov.mvchomework.service.ScannerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private static final String ENTER_ID = "Enter ID for book or 0 is your dont now ID";
    private static final String GET_ALL = "Enter command 'getall' for search your book in list";

    private final BookRepo bookRepo;
    private final ScannerHelper helper;
    private final BookServiceHelper bookServiceHelper;

    public BookServiceImpl(BookRepo bookRepo, ScannerHelper helper, BookServiceHelper bookServiceHelper) {
        this.bookRepo = bookRepo;
        this.helper = helper;
        this.bookServiceHelper = bookServiceHelper;
    }

    @Override
    public BookModel getBook(long id) {
        try {
            Optional<Book> bookOptional = bookRepo.findById(id);
            List<CommentModel> comments = bookOptional.orElseThrow().getListOfComments()
                    .stream()
                    .map(Comment::getCommentForBook)
                    .map(c -> CommentModel.builder().commentForBook(c).build())
                    .collect(Collectors.toList());
            List<AuthorModel> authors = bookOptional.orElseThrow().getAuthor()
                    .stream()
                    .map(Author::getName)
                    .map(a -> AuthorModel.builder().name(a).build())
                    .collect(Collectors.toList());
            return BookModel.builder()
                    .name(bookOptional.orElseThrow().getName())
                    .authors(authors)
                    .genre(bookOptional.orElseThrow().getGenre().getName())
                    .year(bookOptional.orElseThrow().getYear().getDateOfPublish())
                    .comments(comments)
                    .build();
        } catch (NullPointerException e) {
            throw new NoBookException("Book with this id is not exist");
        }
    }

    @Override
    public List<Book> getAllBook() {
        List<Book> books = bookRepo.findAll();
        for (Book book : books) {
            System.out.println(book);
        }
        return books;
    }

    @Override
    @Transactional
    public Book createBook() {
        Book bookFromUser = bookServiceHelper.getBookFromUser(0);
        return bookRepo.save(bookFromUser);
    }

    @Override
    @Transactional
    public void updateBook() {
        log.debug("Start updating book. if you don't want to change the value, click Enter");
        System.out.println(ENTER_ID);
        long id = helper.getIdFromUser();
        helper.getEmptyString();
        if (id > 0) {
            Book bookFromDB = bookRepo.findById(id).orElseThrow();
            BeanUtils.copyProperties(bookServiceHelper.getBookFromUser(id), bookFromDB, "id");
            bookRepo.save(bookFromDB);
        } else {
            System.out.println(GET_ALL);
        }
    }

    @Override
    public void delBook() {
        System.out.println(ENTER_ID);
        long id = helper.getIdFromUser();
        if (id > 0) {
            bookRepo.deleteById(id);
            log.info("Book deleted successfully");
        } else {
            System.out.println(GET_ALL);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments() {
        System.out.println(ENTER_ID);
        long id = helper.getIdFromUser();
        return bookRepo.findById(id).orElseThrow().getListOfComments();
    }
}
