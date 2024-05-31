package com.example.usermanagement.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("""
            select t from VerificationToken t inner join UserAccount u
            on t.user.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<VerificationToken> findAllValidTokenByUserId(Long id);

    Optional<VerificationToken> findByToken(String token);
}
