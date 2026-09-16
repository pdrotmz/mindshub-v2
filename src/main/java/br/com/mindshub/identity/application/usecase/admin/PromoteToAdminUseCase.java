package br.com.mindshub.identity.application.usecase.admin;

import java.util.UUID;

public interface PromoteToAdminUseCase {
    void execute(UUID userId);
}
