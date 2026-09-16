package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.usecase.impl.user.DeleteUserUseCaseImpl;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private DeleteUserUseCaseImpl useCase;

    @Test
    void shouldSoftDeleteUser() {
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(user));

        useCase.execute(uuid);

        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());

        verify(repository).findByUuid(uuid);
        verify(repository).save(user);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(uuid));

        verify(repository).findByUuid(uuid);
        verify(repository, never()).save(any());
    }
}
