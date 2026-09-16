package br.com.mindshub.identity.infrastructure.security.userdetails;

import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock private UserRepository repository;
    @InjectMocks private UserDetailsServiceImpl service;

    @ParameterizedTest
    @EnumSource(Role.class)
    void shouldLoadCredentialsAndRoleForActiveUser(Role role) {
        User user = existingUser(role, true);
        var result = service.loadUserByUsername(user.getEmail());
        assertThat(result.getUsername()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.getAuthorities()).extracting("authority").containsExactly("ROLE_" + role.name());
        verify(repository).findByEmail(user.getEmail());
    }

    @Test
    void shouldDisableAuthenticationForInactiveUser() {
        User user = existingUser(Role.STUDENT, false);
        assertThat(service.loadUserByUsername(user.getEmail()).isEnabled()).isFalse();
    }

    @Test
    void shouldRejectMissingUser() {
        assertThatThrownBy(() -> service.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    private User existingUser(Role role, boolean active) {
        User user = new User();
        user.setEmail("pedro@example.com");
        user.setPassword("encoded-password");
        user.setRole(role);
        user.setActive(active);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        return user;
    }
}
