package br.com.mindshub.identity.infrastructure.mail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringEmailSenderTest {
    @Mock private JavaMailSender mailSender;
    @InjectMocks private SpringEmailSender sender;

    @Test
    void shouldSendVerificationLinkToRequestedRecipient() {
        sender.sendVerificationEmail("pedro@example.com", "verification-token");
        var captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        var message = captor.getValue();
        assertThat(message.getTo()).containsExactly("pedro@example.com");
        assertThat(message.getSubject()).isEqualTo("Mindshub - Verify your email");
        assertThat(message.getText()).contains("/verify-email?token=verification-token");
        verifyNoMoreInteractions(mailSender);
    }

    @Test
    void shouldPropagateDeliveryFailure() {
        var failure = new MailSendException("SMTP unavailable");
        doThrow(failure).when(mailSender).send(any(SimpleMailMessage.class));
        assertThatThrownBy(() -> sender.sendVerificationEmail("pedro@example.com", "token")).isSameAs(failure);
    }
}
