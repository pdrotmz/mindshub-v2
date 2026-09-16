package br.com.mindshub.identity.application.user;
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

import br.com.mindshub.identity.application.usecase.impl.user.UpdateUsersInfoUseCaseImpl;

@ExtendWith(MockitoExtension.class)
class UpdateUsersInfoUseCaseImplTest {
    @Mock private UserRepository repository;
    @InjectMocks private UpdateUsersInfoUseCaseImpl useCase;
    private final String email = "pedro@example.com";
    private final UpdateUsersInfoRequest request = new UpdateUsersInfoRequest("new-name", "new@example.com");

    @Test
    void shouldUpdateAvailableUsernameAndEmail() {
        User user = existingUser();
        useCase.execute(email, request);
        assertEquals(request.username(), user.getUsername());
        assertEquals(request.email(), user.getEmail());
        verify(repository).existsByUsername(request.username());
        verify(repository).existsByEmail(request.email());
        verify(repository).save(user);
    }

    @Test
    void shouldAllowKeepingOwnUsernameAndEmail() {
        User user = existingUser();
        useCase.execute(email, new UpdateUsersInfoRequest(user.getUsername(), email));
        verify(repository, never()).existsByUsername(anyString());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository).save(user);
    }

    @Test
    void shouldRejectTakenUsernameWithoutChangingUser() {
        User user = existingUser();
        when(repository.existsByUsername(request.username())).thenReturn(true);
        assertThrows(UsernameAlreadyRegisteredException.class, () -> useCase.execute(email, request));
        assertEquals("pedro", user.getUsername());
        assertEquals(email, user.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectTakenEmailWithoutChangingUser() {
        User user = existingUser();
        when(repository.existsByEmail(request.email())).thenReturn(true);
        assertThrows(EmailAlreadyRegisteredException.class, () -> useCase.execute(email, request));
        assertEquals("pedro", user.getUsername());
        assertEquals(email, user.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectMissingUser() {
        assertThrows(UserNotFoundException.class, () -> useCase.execute(email, request));
        verify(repository, never()).save(any());
    }

    private User existingUser() {
        User user = new User();
        user.setUsername("pedro");
        user.setEmail(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        return user;
    }
}
