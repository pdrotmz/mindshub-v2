package br.com.mindshub.identity.infrastructure.persistence.adapter;

import br.com.mindshub.identity.infrastructure.persistence.PersistenceTestSupport;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.temporal.ChronoUnit;
import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationTokenAdapterTest extends PersistenceTestSupport {
    @Autowired private EmailVerificationTokenRepository tokens;

    @Test
    void shouldPersistAndReadVerificationToken() {
        var user = createUser("pedro", Role.STUDENT, false);
        var token = EmailVerificationToken.create(user.getUuid());
        token.setCreatedAt(token.getCreatedAt().truncatedTo(ChronoUnit.MICROS));
        token.setExpiresAt(token.getExpiresAt().truncatedTo(ChronoUnit.MICROS));
        var saved = tokens.save(token);
        flushAndClear();
        var found = tokens.findByToken(token.getToken()).orElseThrow();
        assertThat(found.getId()).isPositive().isEqualTo(saved.getId());
        assertThat(found.getUserId()).isEqualTo(user.getUuid());
        assertThat(found.getToken()).isEqualTo(token.getToken());
        assertThat(found.getCreatedAt()).isEqualTo(token.getCreatedAt());
        assertThat(found.getExpiresAt()).isEqualTo(token.getExpiresAt());
        assertThat(found.getUsedAt()).isNull();
        assertThat(found.isUsed()).isFalse();
    }

    @Test
    void shouldPersistConsumptionOfExistingToken() {
        var user = createUser("pedro", Role.STUDENT, false);
        var saved = tokens.save(EmailVerificationToken.create(user.getUuid()));
        saved.markAsUsed();
        saved.setUsedAt(saved.getUsedAt().truncatedTo(ChronoUnit.MICROS));
        tokens.save(saved);
        flushAndClear();
        var found = tokens.findByToken(saved.getToken()).orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.isUsed()).isTrue();
        assertThat(found.getUsedAt()).isEqualTo(saved.getUsedAt());
    }

    @Test
    void shouldReturnEmptyForUnknownToken() {
        assertThat(tokens.findByToken("missing")).isEmpty();
    }
}
