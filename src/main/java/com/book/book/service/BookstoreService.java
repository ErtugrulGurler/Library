package com.book.book.service;

import com.book.book.domain.User;

public interface BookstoreService {

    User buyBook(Long bookID, Long userID);
    //User refundBook(String bookname,String username);
}
