package com.kitap.kitap.Repo;

import com.kitap.kitap.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token , Long> {


}
