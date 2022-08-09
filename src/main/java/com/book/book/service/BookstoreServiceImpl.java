package com.book.book.service;

import com.book.book.Repo.BookRepository;
import com.book.book.Repo.UserRepo;
import com.book.book.domain.Book;

import com.book.book.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BookstoreServiceImpl implements BookstoreService{
    private final BookRepository bookRepository;
    private final UserRepo userRepo;
    @Override
    public User buyBook(Long bookID,Long userID) {
        User user=userRepo.findById(userID).get();
        Book book=bookRepository.findById(bookID).get();
        log.info("here");
        book.setUser(user);
        bookRepository.save(book);
        user.getBooks().add(book);
        //userRepo.save(user);
        log.info("fetching {},{}",user.getUsername(),book.getName());
        return user;
    }

















    /*@Override
    public User refundBook(String bookname,String username) {
        User user = userRepo.getById(Long.parseLong(userId));
        int bookid=Integer.parseInt(bookId);
        Book book = (Book) user.getBooks().get(bookid);
        user.getBooks().remove(book);
        return user;
    }*/
}
