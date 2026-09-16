package br.com.mindshub.identity.presentation.dto.response.user;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(

        // TODO: Document this
        Long id,
        UUID uuid,
        String username,
        String email,
        Role role,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }
}
