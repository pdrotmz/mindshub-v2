package br.com.mindshub.identity.infrastructure.event;

import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringEventPublisherTest {
    @Mock private ApplicationEventPublisher publisher;
    @InjectMocks private SpringEventPublisher adapter;

    @Test
    void shouldForwardVerificationEventToSpring() {
        var event = new EmailVerificationRequestedEvent("pedro@example.com", "token");
        adapter.publish(event);
        verify(publisher).publishEvent(same(event));
        verifyNoMoreInteractions(publisher);
    }
}
