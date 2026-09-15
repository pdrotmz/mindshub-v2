package br.com.mindshub.identity.application.exception;

public class UserAlreadyAdminException extends RuntimeException {
    public UserAlreadyAdminException(String message) {
        super(message);
    }
}
