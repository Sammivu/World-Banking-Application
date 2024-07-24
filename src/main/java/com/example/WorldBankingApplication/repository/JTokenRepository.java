package com.example.WorldBankingApplication.repository;

import com.example.WorldBankingApplication.domain.entity.JToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JTokenRepository extends JpaRepository<JToken, Long> {

    //previous
//    @Query(value = """
//      select t from JToken t inner join UserEntity u\s
//      on t.userEntity.id = u.id\s
//      where u.id = :id and (t.expired = false or t.revoked = false)\s
//      """)
//    List<JToken> findAllValidTokenByUser(Long id);

    @Query("SELECT t FROM JToken t WHERE t.userEntity.id = :userId AND t.expired = false AND t.revoked = false")
    List<JToken> findAllValidTokenByUser(@Param("userId") Long userId);

    Optional<JToken> findByToken(String token);
}
