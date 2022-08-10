package com.book.book.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.book.book.Repo.BookRepository;
import com.book.book.Repo.UserRepo;
import com.book.book.domain.Book;
import com.book.book.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BookstoreServiceImpl implements BookstoreService {
    private final BookRepository bookRepository;
    private final UserRepo userRepo;
    @Override
    public User buyBook(Long bookID, HttpServletRequest request) throws IOException {
        User user=getUser(request);
        if(!user.isEnabled())throw new IOException("Only verified accounts can buy books");
        Book book=bookRepository.findById(bookID).orElseThrow(()-> new IOException("Book not found "));
        book.setUser(user);
        user.getBooks().add(book);
        bookRepository.save(book);
        return user;
    }

    @Override
    public User refundBook(Long bookID, HttpServletRequest request) throws IOException {
        User user=getUser(request);
        if(!user.isEnabled())throw new IOException("Only verified accounts can refund books");
        Book book=bookRepository.findById(bookID).orElseThrow(()-> new IOException("Book not found "));
        book.setUser(user);
        user.getBooks().remove(book);
        bookRepository.save(book);
        return user;
    }

    @Override
    public User getUser(HttpServletRequest request) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                return userRepo.findByUsername(username);
            }else throw new IOException("User not found. ");
        }
}