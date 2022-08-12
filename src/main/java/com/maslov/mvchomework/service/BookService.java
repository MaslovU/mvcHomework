package com.maslov.mvchomework.service;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;

import java.util.List;

public interface BookService {
    BookModel getBook(long id);

    List<Book> getAllBook();

    List<Book> createBook(BookModel book);

    BookModel updateBook(BookModel book, Book boorFromDB);

    void delBook(long id);

    List<Comment> getComments(long id);
}
