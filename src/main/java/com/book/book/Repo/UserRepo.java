package com.book.book.Repo;

import com.book.book.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username)throws RuntimeException;
    User getById(Long id);
    boolean existsByUsername(String username);


}