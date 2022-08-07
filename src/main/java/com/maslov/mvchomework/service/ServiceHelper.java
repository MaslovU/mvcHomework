package com.maslov.mvchomework.service;

import com.maslov.mvchomework.domain.Book;
import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.repository.BookRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceHelper {

    private final ScannerHelper helper;
    private final BookRepo bookRepo;

    public ServiceHelper(ScannerHelper helper, BookRepo bookRepo) {
        this.helper = helper;
        this.bookRepo = bookRepo;
    }

    public int getIdForBook() {
        System.out.println("Enter name for book");
        String nameOfBook = helper.getFromUser();
        List<Book> listOfBooks = bookRepo.getBooksByName(nameOfBook);
        if (!listOfBooks.isEmpty()) {
            for (Book b : listOfBooks) {
                System.out.println(b);
            }
            System.out.println("Find id your book and enter it");
            return helper.getIdFromUser();
        } else {
            return 0;
        }
    }

    public int getCommentId(long idForBook) {
        System.out.println("Choose and enter id of comment");
        for (Comment c : bookRepo.findById(idForBook).get().getListOfComments()) {
            System.out.println(c);
        }
        return helper.getIdFromUser();
    }
}
