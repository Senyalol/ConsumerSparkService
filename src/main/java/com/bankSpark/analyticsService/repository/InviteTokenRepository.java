package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InviteTokenRepository extends JpaRepository<InviteToken, Integer> {

//    Optional<InviteToken> findByToken(String token);

    @Query("SELECT t FROM InviteToken t WHERE t.token = :token")
    Optional<InviteToken> findByToken(@Param("token") String token);

//    Optional<InviteToken> findByTokenAndUsedFalseAndExpiresAtAfter(String token, LocalDateTime now);

    @Query("SELECT t FROM InviteToken t WHERE t.token = :token AND t.used = false AND t.expiresAt > :now")
    Optional<InviteToken> findByTokenAndUsedFalseAndExpiresAtAfter(@Param("token") String token, @Param("now") LocalDateTime now);

    // Удаление просроченных неиспользованных токенов
    @Modifying
    int deleteByUsedFalseAndExpiresAtBefore(Instant now);

    // Считаем, сколько будет удалено
    int countByUsedFalseAndExpiresAtBefore(Instant now);

    //Разбор
//    @Query("SELECT t FROM InviteToken t WHERE t.token = :token AND t.used = false AND t.expiresAt > :now")
//    Optional<InviteToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);

   // int countByUsedFalseAndCreatedAtBefore(LocalDateTime date);

}