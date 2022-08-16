package com.book.book.service;


import com.book.book.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUserByname(String username);

    List<User> getUsers();

    User getUser(Long id);

    String signUpUser(User user);

    void confirm(Long token);

    String enable_user(Long id);

    String disable_user(Long id);

    void checkById(Long id);

    String checkIfEnabled(Long id);

    String deleteUser(Long user_no);

    User putUser(User user);
    User putUser(Long id,User user);
}