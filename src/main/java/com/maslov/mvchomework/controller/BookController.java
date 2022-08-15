package com.maslov.mvchomework.controller;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.model.BookModel;
import com.maslov.mvchomework.service.BookService;
import com.maslov.mvchomework.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String list(Model model) {
        List<Book> books = bookService.getAllBook();
        model.addAttribute("books", books);
        return "listOfBook";
    }

    @GetMapping("/book/{id}")
    public String getBook(@RequestParam("id") long id,
                          Model model) {
        BookModel book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "oneBook";
    }

    @GetMapping("/comments/{bookId}")
    public String getComments(@RequestParam("bookId") long bookId,
                              Model model) {
        List<Comment> comments = bookService.getComments(bookId);
        model.addAttribute("comments", comments);
        return "listOfComments";
    }

    @GetMapping("/create")
    public String create() {
        return "createBook";
    }

    @PostMapping("/create")
    public String createBook(@RequestBody BookModel bookModel,
                             Model model) {
        List<Book> books = bookService.createBook(bookModel);
        model.addAttribute("books", books);
        return "listOfBook";
    }

    @GetMapping("/update")
    public String updateBook(@RequestParam("bookId") long bookId,
                             Model model) {
        BookModel bookModel = bookService.getBook(bookId);
        model.addAttribute("book", bookModel);
        model.addAttribute("id", bookId);
        return "editBook";
    }

    @PostMapping("/update")
    public String updateBook(
            Book book) {
        bookService.updateBook(book);
//        model.addAttribute("book", bookModel);
        return "redirect:/book";
    }

    @DeleteMapping("/delete/{id}")
    public String delEmployee(@PathVariable Long id,
                              Model model) {
        bookService.delBook(id);
        return "delete";
    }
}
