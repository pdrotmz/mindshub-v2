package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.usecase.impl.user.GetCurrentUserUseCaseImpl;
import br.com.mindshub.identity.domain.enums.Role;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCurrentUserUseCaseImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetCurrentUserUseCaseImpl useCase;

    @Test
    void shouldReturnCurrentUser() {
        String email = "pedro@gmail.com";

        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUsername("pedro");
        user.setEmail(email);
        user.setRole(Role.STUDENT);
        user.setActive(true);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = useCase.execute(email);

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(user);
        assertThat(result.getEmail()).isEqualTo(email);

        verify(repository).findByEmail(email);
    }

    @Test
    void shouldThrowWhenCurrentUserDoesNotExist() {
        String email = "unknown@gmail.com";

        when(repository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.execute(email)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User was not found");

        verify(repository).findByEmail(email);
    }
}
