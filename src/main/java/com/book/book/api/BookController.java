package com.book.book.api;

import com.book.book.domain.Book;
import com.book.book.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/all")
    public List<Book> getMappingBook() {
        return bookService.getBook();
    }

    @GetMapping(path = "{id}")
    public Optional<Book> getBookById(@PathVariable("id") Long id) {
        return bookService.getBookById(id);}
    @GetMapping
    public void getBookById() {
        throw new RuntimeException("Please specify the ID of book to be drawn. ");
    }

    @PostMapping
    public Book postMappingBook(@RequestBody Book book) {
        return bookService.postBook(book);
    }

    @DeleteMapping(path = "{book_id}")
    public String deleteMappingBook(@PathVariable("book_id") Long book_no) {
        String deletedBook = bookService.getBookById(book_no).toString();
        bookService.deleteBook(book_no);
        return "Book "+deletedBook+" is deleted. ";
    }
    @DeleteMapping
    public void deleteMappingBook() {
        throw new RuntimeException("Please specify the ID of book to be deleted. ");
    }
    @PutMapping
    public Book putMappingBook(@RequestBody Book book) {
        return bookService.putBook(book);
    }
    @PutMapping(path = "{id}")
    public Book Put(@PathVariable("id") Long id, @RequestBody Book book) {
        return bookService.putBook(id, book);
    }
}