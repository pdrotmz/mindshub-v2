package br.com.mindshub.identity.infrastructure.persistence.mapper;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.infrastructure.persistence.entity.EmailVerificationTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationTokenEntityMapper {

    public EmailVerificationToken toDomain(EmailVerificationTokenEntity entity) {

        EmailVerificationToken domain = new EmailVerificationToken();

        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setToken(entity.getToken());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUsedAt(entity.getUsedAt());

        return domain;
    }

    public EmailVerificationTokenEntity toEntity(EmailVerificationToken domain) {

        EmailVerificationTokenEntity entity = new EmailVerificationTokenEntity();

        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setToken(domain.getToken());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUsedAt(domain.getUsedAt());

        return entity;
    }
}
