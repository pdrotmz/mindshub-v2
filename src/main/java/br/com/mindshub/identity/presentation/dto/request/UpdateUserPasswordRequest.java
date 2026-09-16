package br.com.mindshub.identity.presentation.dto.request;

import br.com.mindshub.identity.presentation.validation.password.PasswordMatches;
import br.com.mindshub.identity.presentation.validation.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches(password = "newPassword", confirmation = "confirmNewPassword")
public record UpdateUserPasswordRequest(

        @Schema(description = "user password", example = "Pedrotomaz@3211")
        @NotBlank(message = "current password must be filled")
        String currentPassword,

        @Schema(description = "user password", example = "Pedrotomaz@32110")
        @NotBlank(message = "new password must be filled")
        @ValidPassword
        String newPassword,

        @Schema(description = "user password confirming", example = "Pedrotomaz@32110")
        @NotBlank(message = "confirm password must be filled")
        String confirmNewPassword
) {
}
