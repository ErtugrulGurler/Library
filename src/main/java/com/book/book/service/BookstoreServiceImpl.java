package com.book.book.service;

import com.book.book.Repo.BookRepository;
import com.book.book.Repo.UserRepo;
import com.book.book.domain.Book;
import com.book.book.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookstoreServiceImpl implements BookstoreService{
    private final BookRepository bookRepository;
    private final UserRepo userRepo;
    @Override
    public User buyBook(String bookId,String userId) {
        Book book = bookRepository.getById(Long.valueOf(bookId));
        User user = userRepo.getById(Long.valueOf(userId));
        user.getBooks().add(book);
        return user;
    }
}
