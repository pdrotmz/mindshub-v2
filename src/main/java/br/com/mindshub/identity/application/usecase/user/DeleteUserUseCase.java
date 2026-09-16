package br.com.mindshub.identity.application.usecase.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void execute(UUID userId);
}
