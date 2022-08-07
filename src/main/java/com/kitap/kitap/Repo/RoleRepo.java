package com.kitap.kitap.Repo;

import com.kitap.kitap.domain.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
    boolean existsByName(String name);
}