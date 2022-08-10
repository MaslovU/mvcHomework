package com.maslov.mvchomework.service;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookModel getBook(long id);

    List<Book> getAllBook();

    Book createBook(BookModel book);

    void updateBook();

    void delBook();

    List<Comment> getComments();
}
