package com.maslov.mvchomework.service;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;

import java.util.List;

public interface BookService {
    Book getBook(long id);

    List<Book> getAllBook();

    Book createBook(BookModel bookModel);

    Book updateBook(BookModel bookModel, Book boorFromDB);

    void delBook(long id);

    List<Comment> getComments(long id);
}
