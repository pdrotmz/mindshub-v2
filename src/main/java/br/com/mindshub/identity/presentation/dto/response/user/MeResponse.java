package br.com.mindshub.identity.presentation.dto.response.user;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;

import java.time.LocalDateTime;

public record MeResponse(
        String username,
        String email,
        Role role,
        boolean status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MeResponse from(User user) {
        return new MeResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
