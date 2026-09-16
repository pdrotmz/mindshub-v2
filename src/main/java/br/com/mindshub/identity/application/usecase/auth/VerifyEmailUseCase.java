package br.com.mindshub.identity.application.usecase.auth;

public interface VerifyEmailUseCase {

    void execute(String token);

    void resend(String token);

    // TODO: create and implement resend email without token expired (string email to input)
}
