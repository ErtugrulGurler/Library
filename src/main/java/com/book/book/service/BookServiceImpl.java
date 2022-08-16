package com.book.book.service;


import com.book.book.Repo.BookRepository;
import com.book.book.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public List<Book> getBook() {
        if (bookRepository.findAll().isEmpty()){throw new RuntimeException("There is no book to show. ");}
        return bookRepository.findAll();
    }
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()-> new RuntimeException("The Book with ID that is entered does not exist. "));
    }
    @Override
    public Book postBook(Book book) {
        if (book.getName()==null|| book.getAuthor()==null||book.getPrice()==null){throw new RuntimeException("Book author, name, or price can not be empty. ");}
        bookRepository.save(book);
        return bookRepository.findById(book.getId()).orElseThrow(()->new RuntimeException("Something went wrong. "));
    }
    @Override
    public void deleteBook(Long book_no) {
        bookRepository.deleteById(book_no);
    }
    @Override
    public Book putBook(Book updateBook) {
        if (updateBook.getId()==null){
            throw new RuntimeException("ID is not entered");
        }
       return changeBook(updateBook);
    }
    @Override
    public Book putBook(Long id, Book updateBook) {
        if (updateBook.getId()==null){
            updateBook.setId(id);
            return putBook(updateBook);
        } else if(!updateBook.getId().equals(id)){
            throw new RuntimeException("Path variable and the book that is entered are not the same");
        }
        return changeBook(updateBook);
    }
    public Book changeBook(Book updateBook){//TEST
        checkByID(updateBook.getId());
        Book dbbook = bookRepository.findById(updateBook.getId()).orElseThrow(()-> new RuntimeException("Book not found. "));

            if (updateBook.getName() == null) {
                updateBook.setName(dbbook.getName());
            } else {
                dbbook.setName(updateBook.getName());
            }
            if (updateBook.getAuthor() == null) {
                updateBook.setAuthor(dbbook.getAuthor());
            } else {
                dbbook.setAuthor(updateBook.getAuthor());
            }
            if (updateBook.getNumber_of_pages() == null) {
                updateBook.setNumber_of_pages(dbbook.getNumber_of_pages());
            } else {
                dbbook.setNumber_of_pages(updateBook.getNumber_of_pages());
            }
            if (updateBook.getPrice() == null) {
                updateBook.setPrice(dbbook.getPrice());
            } else {
                dbbook.setPrice(updateBook.getPrice());
            }

        dbbook = updateBook;
        bookRepository.save(dbbook);
        return bookRepository.findById(dbbook.getId()).orElseThrow(()-> new RuntimeException("Something went wrong."));
    }
    @Override
    public void checkByID(Long book_ID) {
        boolean exists = bookRepository.existsById(book_ID);
        if (!exists) {
            throw new IllegalStateException("The Book with ID that is entered does not exist. ");
        }
    }

}
