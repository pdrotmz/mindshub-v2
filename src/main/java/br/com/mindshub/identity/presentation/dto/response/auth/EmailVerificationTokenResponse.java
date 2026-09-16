package br.com.mindshub.identity.presentation.dto.response.auth;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmailVerificationTokenResponse(
        Long id,
        UUID userId,
        String token,
        LocalDateTime expiresAt,
        LocalDateTime createdAt,
        LocalDateTime usedAt
) {

    public static EmailVerificationTokenResponse from(EmailVerificationToken emailVerificationToken) {
        return new EmailVerificationTokenResponse(
                emailVerificationToken.getId(),
                emailVerificationToken.getUserId(),
                emailVerificationToken.getToken(),
                emailVerificationToken.getExpiresAt(),
                emailVerificationToken.getCreatedAt(),
                emailVerificationToken.getUsedAt()
        );
    }
}
