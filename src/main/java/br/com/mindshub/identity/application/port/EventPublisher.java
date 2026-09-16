package br.com.mindshub.identity.application.port;

public interface EventPublisher {
    void publish(Object event);
}
