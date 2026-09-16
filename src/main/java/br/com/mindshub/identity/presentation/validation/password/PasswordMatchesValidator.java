package br.com.mindshub.identity.presentation.validation.password;

import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String passwordField;
    private String confirmationField;


    @Override
    public void initialize(PasswordMatches annotation) {
        this.passwordField = annotation.password();
        this.confirmationField = annotation.confirmation();
    }

    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext context
    ) {
        if (value == null) {
            return true;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(value);

        Object password = wrapper.getPropertyValue(passwordField);
        Object confirmation = wrapper.getPropertyValue(confirmationField);

        return Objects.equals(password, confirmation);
    }
}
