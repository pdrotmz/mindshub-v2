package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.usecase.impl.user.GetUsersByRoleUseCaseImpl;
import br.com.mindshub.identity.domain.enums.Role;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUsersRoleImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetUsersByRoleUseCaseImpl useCase;

    @Test
    void shouldReturnUsersByRole() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setRole(Role.TEACHER);

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setRole(Role.TEACHER);

        when(repository.findByRole(Role.TEACHER)).thenReturn(List.of(firstUser, secondUser));

        List<User> responses = useCase.execute(Role.TEACHER);

        assertEquals(2, responses.size());

        verify(repository).findByRole(Role.TEACHER);
    }
}
