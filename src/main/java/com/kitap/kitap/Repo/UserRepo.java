package com.kitap.kitap.Repo;

import com.kitap.kitap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;




public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getById(Long id);



    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    void confirm(String email);

}