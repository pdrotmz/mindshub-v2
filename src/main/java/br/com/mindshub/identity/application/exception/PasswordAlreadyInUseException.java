package br.com.mindshub.identity.application.exception;

public class PasswordAlreadyInUseException extends RuntimeException {
    public PasswordAlreadyInUseException(String message) {
        super(message);
    }
}
