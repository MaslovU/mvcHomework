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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig(BookServiceImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookServiceImplTest {
    private static final String TEST = "Test";
    private static final long ID = 11;

    @MockBean
    private BookDataProvider bookDataProvider;
    @MockBean
    private YearDataProvider yearDataProvider;
    @MockBean
    private GenreDataProvider genreDataProvider;
    @MockBean
    private CommentDataProvider commentDataProvider;
    @MockBean
    private AuthorDataProvider authorDataProvider;

    @Autowired
    BookService service;

    @Test
    void getBook() {
        when(bookDataProvider.getBook(anyLong())).thenReturn(any());

        service.getBook(ID);

        verify(bookDataProvider, Mockito.times(1)).getBook(11L);
    }

    @Test
    void getBookWithWrongId() {
        when(bookDataProvider.getBook(2L)).thenThrow(new MaslovBookException("Book with this id is not exist"));

        MaslovBookException exception = Assertions.assertThrows(MaslovBookException.class, () -> {
            service.getBook(2L);
        });

        Assertions.assertEquals("Book with this id is not exist", exception.getMessage());

    }

    @Test
    void getAllBook() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());

        when(bookDataProvider.getAllBook()).thenReturn(books);
        List<Book> list = service.getAllBook();

        assert list.size() > 0;
    }

    @Test
    void createBook() {
        List<String> authorsList = new ArrayList<>();
        authorsList.add("lafore");
        List<String> commentsList = new ArrayList<>();
        commentsList.add("first");

        BookModel book = BookModel.builder().name(TEST).authors(authorsList).genre("study").listOfComments(commentsList).year("2022").build();

        service.createBook(book);

        verify(bookDataProvider, Mockito.times(1)).createBook(any());
    }

    @Test
    void updateBook() {
        List<String> authorsList = new ArrayList<>();
        authorsList.add("lafore");
        List<String> commentsList = new ArrayList<>();
        commentsList.add("first");

        Book bookFromDB = new Book();
        BookModel bookModel = BookModel.builder().name(TEST).authors(authorsList).genre("study").listOfComments(commentsList).year("2022").build();

        when(bookDataProvider.getBook(5L)).thenReturn(Book.builder().name(TEST).build());

        service.updateBook(bookModel, bookFromDB);

        verify(bookDataProvider, Mockito.times(1)).createBook(any());
    }

    @Test
    void delBook() {
        service.delBook(ID);

        verify(bookDataProvider, Mockito.times(1)).deleteById(anyLong());
    }
}
