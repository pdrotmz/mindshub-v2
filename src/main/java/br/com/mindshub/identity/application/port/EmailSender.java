package br.com.mindshub.identity.application.port;

public interface EmailSender {

    void sendVerificationEmail(String recipient, String token);
}
