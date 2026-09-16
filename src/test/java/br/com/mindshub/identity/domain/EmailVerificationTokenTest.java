package br.com.mindshub.identity.domain;

import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTokenTest {

    @Test
    void shouldCreateEmailVerificationToken() {
        UUID userId = UUID.randomUUID();

        LocalDateTime before = LocalDateTime.now();

        EmailVerificationToken token = EmailVerificationToken.create(userId);

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(token);
        assertEquals(userId, token.getUserId());

        assertNotNull(token.getToken());
        assertFalse(token.getToken().isBlank());

        assertNotNull(token.getCreatedAt());
        assertNotNull(token.getExpiresAt());
        assertNull(token.getUsedAt());

        assertFalse(token.getCreatedAt().isBefore(before));
        assertFalse(token.getCreatedAt().isAfter(after));
    }

    @Test
    void shouldCreateTokenWithTwoHourExpiration() {
        UUID userId = UUID.randomUUID();

        EmailVerificationToken token = EmailVerificationToken.create(userId);

        assertEquals(token.getCreatedAt().plusHours(2), token.getExpiresAt());
    }

    @Test
    void shouldReturnFalseWhenTokenIsNotExpired() {
        EmailVerificationToken token = EmailVerificationToken.create(UUID.randomUUID());

        assertFalse(token.isExpired());
    }

    @Test
    void shouldReturnTrueWhenTokenIsExpired() {
        EmailVerificationToken token = new EmailVerificationToken();

        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        assertTrue(token.isExpired());
    }

    @Test
    void shouldReturnFalseWhenTokenWasNotUsed() {
        EmailVerificationToken token =
                EmailVerificationToken.create(UUID.randomUUID());

        assertFalse(token.isUsed());
    }

    @Test
    void shouldMarkTokenAsUsed() {
        EmailVerificationToken token = EmailVerificationToken.create(UUID.randomUUID());

        assertFalse(token.isUsed());
        assertNull(token.getUsedAt());

        token.markAsUsed();

        assertTrue(token.isUsed());
        assertNotNull(token.getUsedAt());
    }

    @Test
    void shouldSetUsedAtWhenTokenIsMarkedAsUsed() {
        EmailVerificationToken token = EmailVerificationToken.create(UUID.randomUUID());

        LocalDateTime before = LocalDateTime.now();

        token.markAsUsed();

        LocalDateTime after = LocalDateTime.now();

        assertFalse(token.getUsedAt().isBefore(before));
        assertFalse(token.getUsedAt().isAfter(after));
    }
}
