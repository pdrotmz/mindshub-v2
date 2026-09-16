package br.com.mindshub.identity.application.event;

public record EmailVerificationRequestedEvent(
        String email,
        String verificationEmail
) {
}
