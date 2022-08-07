package com.book.book.service;

import com.book.book.domain.User;

public interface BookstoreService {

    User buyBook(String bookId, String userId);
}
