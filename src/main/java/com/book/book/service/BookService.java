package com.book.book.service;

import com.book.book.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBook();
    Book getBookById(Long id);
    Book postBook(Book book);
    void deleteBook(Long book_no);
    Book putBook(Book updateBook);
    Book putBook(Long id, Book updateBook);
    void checkByID(Long book_ID);

}
