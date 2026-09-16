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

import br.com.mindshub.identity.application.usecase.impl.admin.PromoteToAdminUseCaseImpl;

@ExtendWith(MockitoExtension.class)
class PromoteToAdminUseCaseImplTest {
    @Mock private UserRepository repository;
    @InjectMocks private PromoteToAdminUseCaseImpl useCase;
    private final UUID uuid = UUID.randomUUID();

    @Test
    void shouldPromoteEligibleUser() {
        User user = userWithRole(Role.TEACHER);
        useCase.execute(uuid);
        assertEquals(Role.ADMIN, user.getRole());
        verify(repository).save(user);
    }

    @Test
    void shouldRejectUserAlreadyPromoted() {
        userWithRole(Role.ADMIN);
        assertThrows(UserAlreadyAdminException.class, () -> useCase.execute(uuid));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectIneligibleRole() {
        User user = userWithRole(Role.STUDENT);
        assertThrows(InvalidUserRoleException.class, () -> useCase.execute(uuid));
        assertEquals(Role.STUDENT, user.getRole());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectMissingUser() {
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> useCase.execute(uuid));
        verify(repository, never()).save(any());
    }

    private User userWithRole(Role role) {
        User user = new User();
        user.setUuid(uuid);
        user.setRole(role);
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(user));
        return user;
    }
}
