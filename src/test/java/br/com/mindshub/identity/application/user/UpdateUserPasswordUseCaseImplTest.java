package br.com.mindshub.identity.application.user;

import br.com.mindshub.identity.application.exception.InvalidCurrentPasswordException;
import br.com.mindshub.identity.application.exception.PasswordAlreadyInUseException;
import br.com.mindshub.identity.application.usecase.impl.user.UpdateUserPasswordUseCaseImpl;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.UpdateUserPasswordRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordUseCaseImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserPasswordUseCaseImpl useCase;

    @Test
    void shouldUpdatePassword() {
        String email = "pedro@gmail.com";
        String currentPassword = "Old@123";
        String newPassword = "New@123";
        String encodedCurrentPassword = "encoded-old-password";
        String encodedNewPassword = "encoded-new-password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedCurrentPassword);

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                currentPassword,
                newPassword,
                newPassword
        );

        when(repository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                currentPassword,
                encodedCurrentPassword
        )).thenReturn(true);

        when(passwordEncoder.matches(
                newPassword,
                encodedCurrentPassword
        )).thenReturn(false);

        when(passwordEncoder.encode(newPassword))
                .thenReturn(encodedNewPassword);

        useCase.execute(email, request);

        assertThat(user.getPassword())
                .isEqualTo(encodedNewPassword);

        verify(repository).findByEmail(email);
        verify(passwordEncoder).matches(
                currentPassword,
                encodedCurrentPassword
        );
        verify(passwordEncoder).matches(
                newPassword,
                encodedCurrentPassword
        );
        verify(passwordEncoder).encode(newPassword);
        verify(repository).save(user);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        String email = "unknown@gmail.com";
        String currentPassword = "Old@123";
        String newPassword = "New@123";

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                currentPassword,
                newPassword,
                newPassword
        );

        when(repository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.execute(email, request)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User was not found.");

        verify(repository).findByEmail(email);
        verify(repository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowWhenCurrentPasswordIsIncorrect() {
        String email = "pedro@gmail.com";
        String currentPassword = "Wrong@123";
        String newPassword = "New@123";
        String encodedCurrentPassword = "encoded-old-password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedCurrentPassword);

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                currentPassword,
                newPassword,
                newPassword
        );

        when(repository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                currentPassword,
                encodedCurrentPassword
        )).thenReturn(false);

        assertThatThrownBy(() ->
                useCase.execute(email, request)
        )
                .isInstanceOf(InvalidCurrentPasswordException.class);

        verify(repository).findByEmail(email);
        verify(passwordEncoder).matches(
                currentPassword,
                encodedCurrentPassword
        );

        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowWhenNewPasswordIsSameAsCurrentPassword() {
        String email = "pedro@gmail.com";
        String currentPassword = "Password@123";
        String newPassword = "Password@123";
        String encodedCurrentPassword = "encoded-current-password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedCurrentPassword);

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                currentPassword,
                newPassword,
                newPassword
        );

        when(repository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                currentPassword,
                encodedCurrentPassword
        )).thenReturn(true);

        assertThatThrownBy(() ->
                useCase.execute(email, request)
        )
                .isInstanceOf(PasswordAlreadyInUseException.class);

        verify(repository).findByEmail(email);

        verify(passwordEncoder, times(2)).matches(
                currentPassword,
                encodedCurrentPassword
        );

        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
    }
}