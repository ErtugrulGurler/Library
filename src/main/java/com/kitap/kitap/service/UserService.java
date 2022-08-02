package com.kitap.kitap.service;

import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    void takeRoleFromUser(String username,String roleName);

    User getUser(String username);

    List<User> getUsers();

    String signUpUser(User user);

    void confirm(Long token);



}