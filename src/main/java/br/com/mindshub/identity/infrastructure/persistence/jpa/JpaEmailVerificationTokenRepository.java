package br.com.mindshub.identity.infrastructure.persistence.jpa;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.infrastructure.persistence.entity.EmailVerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaEmailVerificationTokenRepository extends JpaRepository<EmailVerificationTokenEntity, Long> {

    Optional<EmailVerificationTokenEntity> findByToken(String token);
}
