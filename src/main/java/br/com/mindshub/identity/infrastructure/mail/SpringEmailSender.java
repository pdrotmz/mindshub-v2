package br.com.mindshub.identity.infrastructure.mail;

import br.com.mindshub.identity.application.port.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String recipient, String token) {

        String verificationUrl ="/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject("Mindshub - Verify your email");
        message.setText("""
                Welcome to MindsHub!

                Verify your email by accessing the link below:

                %s

                This link expires in 5 minutes.
                """.formatted(verificationUrl));

        mailSender.send(message);
    }
}
