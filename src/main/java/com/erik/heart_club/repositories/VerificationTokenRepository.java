package com.erik.heart_club.repositories;

import com.erik.heart_club.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    public VerificationToken getVerificationTokenByToken(String token);
}
