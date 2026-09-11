package br.com.mindshub.identity.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(

        @Schema(description = "User email", example = "user@gmail.com")
        @Email
        @NotBlank(message = "Email must be filled")
        String email,

        @Schema(description = "User password", example = "Pedrotomaz@2453")
        @NotBlank(message = "Password must be filled")
        String password
) {
}
