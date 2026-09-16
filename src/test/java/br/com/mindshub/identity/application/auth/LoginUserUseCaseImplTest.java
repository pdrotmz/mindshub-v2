package br.com.mindshub.identity.application.auth;
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

import br.com.mindshub.identity.application.usecase.impl.auth.LoginUserUseCaseImpl;
import br.com.mindshub.identity.infrastructure.security.jwt.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @InjectMocks private LoginUserUseCaseImpl useCase;
    private final LoginUserRequest request = new LoginUserRequest("pedro@example.com", "Password@123");

    @Test
    void shouldAuthenticateCredentialsAndReturnJwt() {
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername(request.email())
                .password("encoded").roles("STUDENT").build();
        when(authenticationManager.authenticate(any())).thenReturn(
                UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities()));
        when(jwtService.generateToken(user)).thenReturn("signed-token");
        assertEquals("signed-token", useCase.execute(request).token());
        ArgumentCaptor<Authentication> credentials = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(credentials.capture());
        assertEquals(request.email(), credentials.getValue().getPrincipal());
        assertEquals(request.password(), credentials.getValue().getCredentials());
    }

    @Test
    void shouldTranslateBadCredentialsAndNotGenerateToken() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid"));
        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(request));
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldNotGenerateTokenForDisabledAccount() {
        when(authenticationManager.authenticate(any())).thenThrow(new DisabledException("Disabled"));
        assertThrows(DisabledException.class, () -> useCase.execute(request));
        verifyNoInteractions(jwtService);
    }
}
