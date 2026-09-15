package br.com.mindshub.identity.presentation.dto.response.auth;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponse(

        // TODO: Document this
        Long id,
        UUID uuid,
        String username,
        String email,
        Role role,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static RegisterUserResponse from(User user) {
        return new RegisterUserResponse(
            user.getId(),
            user.getUuid(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
