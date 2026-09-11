package br.com.mindshub.identity.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmailVerificationToken {

    private Long id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    public static EmailVerificationToken create(UUID userId) {
        LocalDateTime now = LocalDateTime.now();

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedAt(now);
        token.setExpiresAt(now.plusMinutes(5));

        return token;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public void markAsUsed() {
        this.usedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
