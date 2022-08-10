package com.book.book.api;

import com.book.book.domain.User;
import com.book.book.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class BookstoreController {
    private final BookstoreService bookstoreService;

    @GetMapping(path = "/buy")
    public User buyBook(@RequestParam("BookID") Long bookID,HttpServletRequest request) throws IOException {
        return bookstoreService.buyBook(bookID,request);
    }
    @GetMapping(path = "/refund")
    public User refundBook(@RequestParam("BookID") Long bookID, HttpServletRequest request) throws IOException{
        return bookstoreService.refundBook(bookID,request);
    }
}
