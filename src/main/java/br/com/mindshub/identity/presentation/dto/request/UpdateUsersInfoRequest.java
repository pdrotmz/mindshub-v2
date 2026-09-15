package br.com.mindshub.identity.presentation.dto.request;

public record UpdateUsersInfoRequest(
        String username,
        String email
) {
}
