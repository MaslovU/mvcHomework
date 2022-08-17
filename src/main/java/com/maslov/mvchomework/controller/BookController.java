package com.maslov.mvchomework.controller;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public List<Book> list() {
        return bookService.getAllBook();
    }

    @GetMapping("{id}")
    public Book getBook(@RequestParam("id") long id) {
        return bookService.getBook(id);
    }

    @GetMapping("{bookId}")
    public List<Comment> getComments(@RequestParam("bookId") long bookId) {
        return bookService.getComments(bookId);
    }

    @PostMapping
    public Book createBook(@RequestBody BookModel book) {
        return bookService.createBook(book);
    }

    @PutMapping("{id}")
    public Book updateBook(@PathVariable("id") Book bookFromDB,
                           @RequestBody BookModel bookModel) {
        return bookService.updateBook(bookModel, bookFromDB);
    }

    @DeleteMapping("{id}")
    public void delEmployee(@PathVariable Long id) {
        bookService.delBook(id);
    }
}
