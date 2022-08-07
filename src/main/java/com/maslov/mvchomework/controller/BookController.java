package com.maslov.mvchomework.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Views;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Book> list() {
        return service.getAllBook();
    }

    @GetMapping("/getbook")
    public BookModel getBook(@RequestParam("id") long id) {
        return service.getBook(id);
    }
}
