package br.com.mindshub.identity.application.exception;

public class EmailVerificationTokenNotFoundException extends RuntimeException {
    public EmailVerificationTokenNotFoundException(String message) {
        super(message);
    }
}
