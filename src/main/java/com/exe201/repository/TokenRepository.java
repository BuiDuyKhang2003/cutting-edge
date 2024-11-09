package com.exe201.repository;


import com.exe201.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.user.userId = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokensByUser(UUID userId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshTokenAndRevokedFalseAndExpiredFalse(String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.revoked = true AND t.expired = true")
    void deleteTokensByRevokedTrueAndExpiredTrue();

}
