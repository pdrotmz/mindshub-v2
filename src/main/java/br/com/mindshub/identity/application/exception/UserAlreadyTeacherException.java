package br.com.mindshub.identity.application.exception;

public class UserAlreadyTeacherException extends RuntimeException {
    public UserAlreadyTeacherException(String message) {
        super(message);
    }
}
