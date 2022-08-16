package com.book.book.service;

import com.book.book.domain.User;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface BookstoreService {

    User buyBook(Long bookID, HttpServletRequest request) throws IOException;
    User refundBook(Long bookID, HttpServletRequest request) throws IOException;
    User getUser(HttpServletRequest request) throws IOException;
}
