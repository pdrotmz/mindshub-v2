package br.com.mindshub.identity.presentation.validation.password;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void shouldAcceptPasswordMeetingEveryRequirement() {
        assertTrue(validator.isValid("Strong@123", null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Aa1!", "lowercase1!", "UPPERCASE1!", "NoDigits!!", "NoSpecial123", "        "})
    void shouldRejectPasswordMissingARequirement(String password) {
        assertFalse(validator.isValid(password, null));
    }
}
