package br.com.mindshub.identity.infrastructure.persistence.adapter;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import br.com.mindshub.identity.infrastructure.persistence.entity.EmailVerificationTokenEntity;
import br.com.mindshub.identity.infrastructure.persistence.jpa.JpaEmailVerificationTokenRepository;
import br.com.mindshub.identity.infrastructure.persistence.mapper.EmailVerificationTokenEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationTokenAdapter implements EmailVerificationTokenRepository {

    private final JpaEmailVerificationTokenRepository tokenRepository;
    private final EmailVerificationTokenEntityMapper mapper;

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken token) {
        EmailVerificationTokenEntity entity = mapper.toEntity(token);

        EmailVerificationTokenEntity savedEntity = tokenRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}
