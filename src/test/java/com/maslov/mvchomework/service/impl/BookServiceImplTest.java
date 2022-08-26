package com.maslov.mvchomework.service.impl;

import com.maslov.mvchomework.domain.Author;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.repository.AuthorRepo;
import com.maslov.mvchomework.repository.BookRepo;
import com.maslov.mvchomework.repository.CommentRepo;
import com.maslov.mvchomework.repository.GenreRepo;
import com.maslov.mvchomework.repository.YearRepo;
import com.maslov.mvchomework.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig(BookServiceImpl.class)
class BookServiceImplTest {
    private static final String TEST = "Test";
    private static final long ID = 7;

    @MockBean
    private BookRepo bookRepo;
    @MockBean
    private YearRepo yearRepo;
    @MockBean
    private GenreRepo genreRepo;
    @MockBean
    private CommentRepo commentRepo;
    @MockBean
    private AuthorRepo authorRepo;

    @Autowired
    BookService service;

    //todo need fixed test
//    @Test
//    void getBook() {
//
//        service.getBook(ID);
//
//        verify(bookRepo, Mockito.times(0)).findById(7L);
//
//    }

    @Test
    void createBook() {
        List<String> authorsList = new ArrayList<>();
        authorsList.add("lafore");
        List<String> commentsList = new ArrayList<>();
        commentsList.add("first");

        BookModel book = BookModel.builder().name(TEST).authors(authorsList).genre("study").listOfComments(commentsList).year("2022").build();

        service.createBook(book);

        verify(bookRepo, Mockito.times(1)).save(any());
    }

    @Test
    void updateBook() {
        List<String> authorsList = new ArrayList<>();
        authorsList.add("lafore");
        List<String> commentsList = new ArrayList<>();
        commentsList.add("first");

        Book bookFromDB = new Book();
        BookModel bookModel = BookModel.builder().name(TEST).authors(authorsList).genre("study").listOfComments(commentsList).year("2022").build();

        when(bookRepo.findById(5L)).thenReturn(Optional.ofNullable(Book.builder().name(TEST).build()));

        service.updateBook(bookModel, bookFromDB);

        verify(bookRepo, Mockito.times(1))
                .save(any());
    }

    @Test
    void delBook() {

        service.delBook(ID);

        verify(bookRepo, Mockito.times(1)).deleteById(anyLong());
    }
}
