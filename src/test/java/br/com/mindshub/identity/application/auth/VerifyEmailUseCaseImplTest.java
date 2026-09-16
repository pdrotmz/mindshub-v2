package br.com.mindshub.identity.application.auth;
import br.com.mindshub.identity.application.exception.*;
import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import br.com.mindshub.identity.application.port.EventPublisher;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.repository.*;
import br.com.mindshub.identity.domain.exception.*;
import br.com.mindshub.identity.presentation.dto.request.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.mindshub.identity.application.usecase.impl.auth.VerifyEmailUseCaseImpl;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class VerifyEmailUseCaseImplTest {
    @Mock private UserRepository userRepository;
    @Mock private EmailVerificationTokenRepository emailRepository;
    @Mock private EventPublisher eventPublisher;
    @InjectMocks private VerifyEmailUseCaseImpl useCase;
    private final UUID uuid = UUID.randomUUID();

    @Test
    void shouldActivateUserAndConsumeToken() {
        EmailVerificationToken token = existingToken();
        User user = existingUser();
        useCase.execute(token.getToken());
        assertTrue(user.isActive());
        assertTrue(token.isUsed());
        verify(userRepository).save(user);
        verify(emailRepository).save(token);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldRejectMissingToken() {
        assertThrows(EmailVerificationTokenNotFoundException.class, () -> useCase.execute("missing"));
        verifyNoInteractions(userRepository, eventPublisher);
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldRejectUsedToken() {
        EmailVerificationToken token = existingToken();
        token.markAsUsed();
        assertThrows(EmailAlreadyVerifiedException.class, () -> useCase.execute(token.getToken()));
        verifyNoInteractions(userRepository, eventPublisher);
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldRejectExpiredToken() {
        EmailVerificationToken token = existingToken();
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        assertThrows(EmailVerificationTokenExpiredException.class, () -> useCase.execute(token.getToken()));
        assertFalse(token.isUsed());
        verifyNoInteractions(userRepository, eventPublisher);
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldRejectMissingUserWithoutConsumingToken() {
        EmailVerificationToken token = existingToken();
        assertThrows(UserNotFoundException.class, () -> useCase.execute(token.getToken()));
        assertFalse(token.isUsed());
        verify(emailRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldResendWithNewTokenAndInvalidateOldToken() {
        EmailVerificationToken old = existingToken();
        old.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        User user = existingUser();
        useCase.resend(old.getToken());
        ArgumentCaptor<EmailVerificationToken> tokens = ArgumentCaptor.forClass(EmailVerificationToken.class);
        verify(emailRepository, times(2)).save(tokens.capture());
        EmailVerificationToken fresh = tokens.getAllValues().get(1);
        assertTrue(old.isUsed());
        assertNotEquals(old.getToken(), fresh.getToken());
        assertEquals(uuid, fresh.getUserId());
        assertFalse(fresh.isUsed());
        assertFalse(fresh.isExpired());
        verify(eventPublisher).publish(new EmailVerificationRequestedEvent(user.getEmail(), fresh.getToken()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotResendForActiveUser() {
        EmailVerificationToken token = existingToken();
        existingUser().setActive(true);
        assertThrows(EmailAlreadyVerifiedException.class, () -> useCase.resend(token.getToken()));
        assertFalse(token.isUsed());
        verify(emailRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotResendForMissingToken() {
        assertThrows(EmailVerificationTokenNotFoundException.class, () -> useCase.resend("missing"));
        verifyNoInteractions(userRepository, eventPublisher);
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldNotResendForMissingUser() {
        EmailVerificationToken token = existingToken();
        assertThrows(UserNotFoundException.class, () -> useCase.resend(token.getToken()));
        verify(emailRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    private EmailVerificationToken existingToken() {
        EmailVerificationToken token = EmailVerificationToken.create(uuid);
        when(emailRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));
        return token;
    }

    private User existingUser() {
        User user = new User();
        user.setUuid(uuid);
        user.setEmail("pedro@example.com");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        return user;
    }
}
