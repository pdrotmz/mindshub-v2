package br.com.mindshub.identity.domain.repository;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository {

    Optional<EmailVerificationToken> findByToken(String token);

    EmailVerificationToken save(EmailVerificationToken verificationToken);
}
