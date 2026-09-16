package br.com.mindshub.identity.presentation.dto.request;

import br.com.mindshub.identity.presentation.validation.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAdminRequest(

        @Schema(description = "User email", example = "usern@gmail.com")
        @NotBlank(message = "email must be filled")
        @Email
        String email,

        @Schema(description = "user password", example = "Pedrotomaz@3211")
        @NotBlank(message = "password must be filled")
        @ValidPassword
        String password
) {
}
