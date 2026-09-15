package br.com.mindshub.identity.application.exception;

public class InvalidUserRoleException extends RuntimeException {
    public InvalidUserRoleException(String message) {
        super(message);
    }
}
