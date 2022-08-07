package com.kitap.kitap.Repo;

import com.kitap.kitap.domain.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitapRepository extends JpaRepository<Kitap,Long> {
}
