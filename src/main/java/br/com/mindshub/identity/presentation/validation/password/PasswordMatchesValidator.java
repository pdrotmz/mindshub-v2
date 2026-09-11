package br.com.mindshub.identity.presentation.validation.password;

import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserRequest> {

    @Override
    public boolean isValid(
            RegisterUserRequest request,
            ConstraintValidatorContext context
    ) {
        if (request == null) {
            return true;
        }

        return Objects.equals(
                request.password(),
                request.confirmPassword()
        );
    }
}
