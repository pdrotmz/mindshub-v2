package br.com.mindshub.identity.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private static final String SECRET = "PI4aEEtfFTHJStJ8cTkA5xYugpUdW5Oo6unDhirPNBvJOBCVBEZ3ycLeulfvHapVwJ5IHy2BRsnLGLTkgyduSn";
    private final JwtService service = new JwtService(SECRET, 3600000L);
    private final UserDetails user = User.withUsername("pedro@example.com").password("encoded").roles("STUDENT").build();

    @Test
    void shouldGenerateSignedTokenWithSubjectAndExpiration() {
        Instant before = Instant.now().minusSeconds(1);
        String token = service.generateToken(user);
        Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token).getPayload();
        assertEquals(user.getUsername(), claims.getSubject());
        assertFalse(claims.getIssuedAt().toInstant().isBefore(before));
        assertFalse(claims.getIssuedAt().toInstant().isAfter(Instant.now()));
        assertEquals(3600000L, claims.getExpiration().getTime() - claims.getIssuedAt().getTime());
        assertEquals(user.getUsername(), service.extractUsername(token));
        assertEquals(claims.getExpiration(), service.extractClaim(token, Claims::getExpiration));
        assertTrue(service.isTokenValid(token, user));
        assertFalse(claims.containsKey("password"));
    }

    @Test
    void shouldRejectTokenForAnotherUser() {
        UserDetails other = User.withUsername("other@example.com").password("encoded").roles("STUDENT").build();
        assertFalse(service.isTokenValid(service.generateToken(user), other));
    }

    @Test
    void shouldRejectExpiredToken() {
        String token = new JwtService(SECRET, -60000L).generateToken(user);
        assertFalse(service.isTokenValid(token, user));
        assertThrows(ExpiredJwtException.class, () -> service.extractAllClaims(token));
    }

    @Test
    void shouldRejectTokenSignedWithAnotherKey() {
        String token = new JwtService("another-test-secret-with-at-least-32-bytes", 3600000L).generateToken(user);
        assertFalse(service.isTokenValid(token, user));
        assertThrows(JwtException.class, () -> service.extractAllClaims(token));
    }

    @Test
    void shouldRejectTamperedPayload() {
        String token = service.generateToken(user);
        String[] parts = token.split("\\.");
        String payload = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(
                "{\"sub\":\"attacker@example.com\",\"exp\":4102444800}".getBytes(StandardCharsets.UTF_8));
        assertFalse(service.isTokenValid(parts[0] + "." + payload + "." + parts[2], user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalid-token", "a.b.c", " "})
    void shouldRejectMalformedToken(String token) {
        assertFalse(service.isTokenValid(token, user));
    }

    @Test
    void shouldRejectWeakSigningKey() {
        assertThrows(WeakKeyException.class, () -> new JwtService("short", 3600000L));
    }
}
