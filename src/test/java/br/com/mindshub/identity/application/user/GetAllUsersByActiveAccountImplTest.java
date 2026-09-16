package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.usecase.impl.user.GetAllUsersByActiveAccountUseCaseImpl;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllUsersByActiveAccountImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetAllUsersByActiveAccountUseCaseImpl useCase;


    @Test
    void shouldReturnUsersByActiveAccount() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setActive(true);

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setActive(true);

        when(repository.findByActive(true)).thenReturn(List.of(firstUser, secondUser));

        List<User> responses = useCase.execute(true);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        verify(repository).findByActive(true);
    }

    @Test
    void shouldReturnUsersByDeactivateAccount() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setActive(false);

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setActive(false);

        when(repository.findByActive(false)).thenReturn(List.of(firstUser, secondUser));

        List<User> responses = useCase.execute(false);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        verify(repository).findByActive(false);
    }
}
