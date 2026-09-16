package br.com.mindshub.identity.application.event;

import br.com.mindshub.identity.application.event.listener.UserRegisteredEventListener;
import br.com.mindshub.identity.application.port.EmailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserRegisteredEventListenerTest {
    @Mock private EmailSender emailSender;
    @InjectMocks private UserRegisteredEventListener listener;

    @Test
    void shouldSendVerificationEmailToEventRecipient() {
        listener.handle(new EmailVerificationRequestedEvent("pedro@example.com", "verification-token"));
        verify(emailSender).sendVerificationEmail("pedro@example.com", "verification-token");
        verifyNoMoreInteractions(emailSender);
    }
}
