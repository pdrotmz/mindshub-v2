package br.com.mindshub.identity.infrastructure.security.jwt;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private FilterChain chain;
    @InjectMocks private JwtAuthenticationFilter filter;
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final UserDetails user = User.withUsername("pedro@example.com").password("encoded").roles("STUDENT").build();

    @BeforeEach
    void setUp() { SecurityContextHolder.clearContext(); }

    @AfterEach
    void tearDown() { SecurityContextHolder.clearContext(); }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Basic credentials", "bearer token"})
    void shouldContinueWithoutBearerToken(String header) throws Exception {
        if (header != null) request.addHeader("Authorization", header);
        filter.doFilter(request, response, chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, userDetailsService);
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateValidTokenWithUserAuthorities() throws Exception {
        bearerToken();
        when(jwtService.extractUsername("token")).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.isTokenValid("token", user)).thenReturn(true);
        filter.doFilter(request, response, chain);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertSame(user, authentication.getPrincipal());
        assertEquals(java.util.Set.copyOf(user.getAuthorities()), java.util.Set.copyOf(authentication.getAuthorities()));
        assertNull(authentication.getCredentials());
        assertTrue(authentication.isAuthenticated());
        assertNotNull(authentication.getDetails());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateInvalidToken() throws Exception {
        bearerToken();
        when(jwtService.extractUsername("token")).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.isTokenValid("token", user)).thenReturn(false);
        filter.doFilter(request, response, chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldRejectJwtParsingErrorsWithoutContinuingChain() throws Exception {
        bearerToken();
        when(jwtService.extractUsername("token")).thenThrow(new MalformedJwtException("Invalid"));
        filter.doFilter(request, response, chain);
        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(chain, userDetailsService);
    }

    @Test
    void shouldRejectUnknownUserWithoutContinuingChain() throws Exception {
        bearerToken();
        when(jwtService.extractUsername("token")).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenThrow(new UsernameNotFoundException("Missing"));
        filter.doFilter(request, response, chain);
        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(chain);
    }

    @Test
    void shouldKeepExistingAuthentication() throws Exception {
        bearerToken();
        var authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(jwtService.extractUsername("token")).thenReturn(user.getUsername());
        filter.doFilter(request, response, chain);
        assertSame(authentication, SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(userDetailsService);
        verify(jwtService, never()).isTokenValid(anyString(), any());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldContinueWithoutSubject() throws Exception {
        bearerToken();
        when(jwtService.extractUsername("token")).thenReturn(null);
        filter.doFilter(request, response, chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(userDetailsService);
        verify(chain).doFilter(request, response);
    }

    private void bearerToken() { request.addHeader("Authorization", "Bearer token"); }
}
