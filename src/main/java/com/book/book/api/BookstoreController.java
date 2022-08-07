package com.book.book.api;

import com.book.book.domain.User;
import com.book.book.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class BookstoreController {
    private final BookstoreService bookstoreService;

    @GetMapping(path = "/buy")
    public User buyBook(@RequestParam("BookID") String bookId, @RequestParam("UserID") String userId){
        return bookstoreService.buyBook(bookId,userId);
    }
}
