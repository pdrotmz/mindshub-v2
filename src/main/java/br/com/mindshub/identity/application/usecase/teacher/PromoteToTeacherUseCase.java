package br.com.mindshub.identity.application.usecase.teacher;

import java.util.UUID;

public interface PromoteToTeacherUseCase {
    void execute(UUID userId);
}
