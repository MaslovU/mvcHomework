package com.maslov.mvchomework.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Views;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    public BookController(BookService service, CommentService commentService) {
        this.bookService = service;
        this.commentService = commentService;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Book> list() {
        return bookService.getAllBook();
    }

    @GetMapping("/getbook")
    public BookModel getBook(@RequestParam("id") long id) {
        return bookService.getBook(id);
    }

    public Book createBook(@RequestBody BookModel bookModel) {
        return bookService.createBook(bookModel);
    }
}
