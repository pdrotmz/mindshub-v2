package br.com.mindshub.identity.domain.exception;

public class EmailVerificationTokenExpiredException extends RuntimeException {
    public EmailVerificationTokenExpiredException(String message) {
        super(message);
    }
}
