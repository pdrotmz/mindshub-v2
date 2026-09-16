package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.usecase.impl.user.GetAllUsersUseCaseImpl;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.response.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllUsersUseCaseImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetAllUsersUseCaseImpl useCase;

    @Test
    void shouldReturnAllUsers() {
        User firstUser = new User();
        firstUser.setId(1L);

        User secondUser = new User();
        firstUser.setId(2L);

        when(repository.findAll()).thenReturn(List.of(firstUser, secondUser));

        List<User> responses = useCase.execute();

        assertEquals(2, responses.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUsers() {

        when(repository.findAll()).thenReturn(List.of());

        List<User> responses = useCase.execute();

        assertTrue(responses.isEmpty());

        verify(repository).findAll();
    }
}
