package com.maslov.mvchomework.controller;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class BookController {

    private final BookService bookService;

    public BookController(BookService service) {
        this.bookService = service;
    }

    @GetMapping("books")
    public List<Book> list() {
        return bookService.getAllBook();
    }

    @GetMapping("book/onebook")
    public Book getBook(@RequestParam("id") long id) {
        return bookService.getBook(id);
    }

    @GetMapping("book/comment/id")
    public List<Comment> getComments(@RequestParam("id") long bookId) {
        return bookService.getComments(bookId);
    }

    @PostMapping("books")
    public Book createBook(@RequestBody BookModel book) {
        return bookService.createBook(book);
    }

    @PutMapping("(books/{id}")
    public Book updateBook(@PathVariable("id") Book bookFromDB,
                           @RequestBody BookModel bookModel) {
        return bookService.updateBook(bookModel, bookFromDB);
    }

    @DeleteMapping("{id}")
    public void delEmployee(@PathVariable Long id) {
        bookService.delBook(id);
    }
}
