package com.terxiel.store.repositories;

import com.terxiel.store.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    @Query("SELECT b FROM BlacklistedToken b WHERE b.token=:token")
    Optional<BlacklistedToken> findByToken(@Param("token") String token);
}
