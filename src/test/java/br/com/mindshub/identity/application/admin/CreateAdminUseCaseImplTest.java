package br.com.mindshub.identity.application.admin;
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

import br.com.mindshub.identity.application.usecase.impl.admin.CreateAdminUseCaseImpl;

@ExtendWith(MockitoExtension.class)
class CreateAdminUseCaseImplTest {
    @Mock private UserRepository repository;
    @Mock private EmailVerificationTokenRepository emailRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EventPublisher eventPublisher;
    @InjectMocks private CreateAdminUseCaseImpl useCase;
    private final RegisterAdminRequest request = new RegisterAdminRequest("pedro@example.com", "Password@123");

    @Test
    void shouldCreateInactiveUserAndPublishVerificationToken() {
        UUID uuid = UUID.randomUUID();
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUuid(uuid);
            return user;
        });

        var response = useCase.execute(request);

        ArgumentCaptor<User> users = ArgumentCaptor.forClass(User.class);
        verify(repository).save(users.capture());
        assertEquals("pedro", users.getValue().getUsername());
        assertEquals(request.email(), users.getValue().getEmail());
        assertEquals("encoded-password", users.getValue().getPassword());
        assertEquals(Role.ADMIN, users.getValue().getRole());
        assertFalse(users.getValue().isActive());
        assertEquals(uuid, response.uuid());
        assertEquals(Role.ADMIN, response.role());
        assertEquals(request.email(), response.email());
        assertFalse(response.isActive());
        ArgumentCaptor<EmailVerificationToken> tokens = ArgumentCaptor.forClass(EmailVerificationToken.class);
        verify(emailRepository).save(tokens.capture());
        EmailVerificationToken token = tokens.getValue();
        assertEquals(uuid, token.getUserId());
        assertNotNull(token.getToken());
        assertFalse(token.isUsed());
        assertFalse(token.isExpired());
        verify(eventPublisher).publish(new EmailVerificationRequestedEvent(request.email(), token.getToken()));
        verify(passwordEncoder).encode(request.password());
    }

    @Test
    void shouldRejectAlreadyRegisteredEmailWithoutSideEffects() {
        when(repository.existsByEmail(request.email())).thenReturn(true);
        assertThrows(EmailAlreadyRegisteredException.class, () -> useCase.execute(request));
        verify(repository, never()).save(any());
        verifyNoInteractions(passwordEncoder, emailRepository, eventPublisher);
    }
}
