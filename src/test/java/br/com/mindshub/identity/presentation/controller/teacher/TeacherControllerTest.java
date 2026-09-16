package br.com.mindshub.identity.presentation.controller.teacher;
import br.com.mindshub.identity.presentation.controller.ControllerTestSupport;
import br.com.mindshub.identity.presentation.dto.request.*;
import br.com.mindshub.identity.application.exception.*;
import br.com.mindshub.identity.domain.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import br.com.mindshub.identity.application.usecase.teacher.*;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest extends ControllerTestSupport {
    @MockitoBean private CreateTeacherUseCase createUseCase;
    @MockitoBean private PromoteToTeacherUseCase promoteUseCase;
    private final UUID uuid = UUID.randomUUID();
    private final RegisterTeacherRequest request = new RegisterTeacherRequest("pedro@example.com", "Password@123");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateTeacher() throws Exception {
        mvc.perform(post("/api/v1/teachers/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andExpect(content().string(""));
        verify(createUseCase).execute(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldPromoteUser() throws Exception {
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid)).andExpect(status().isOk());
        verify(promoteUseCase).execute(uuid);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectInvalidRegistration() throws Exception {
        mvc.perform(post("/api/v1/teachers/create").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalid\",\"password\":\"weak\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(createUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnConflictForDuplicateEmail() throws Exception {
        when(createUseCase.execute(request)).thenThrow(new EmailAlreadyRegisteredException("Email already registered."));
        mvc.perform(post("/api/v1/teachers/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()).andExpect(jsonPath("message").value("Email already registered."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundForMissingUser() throws Exception {
        doThrow(new UserNotFoundException("User not found.")).when(promoteUseCase).execute(uuid);
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnConflictForAlreadyPromotedUser() throws Exception {
        doThrow(new UserAlreadyTeacherException("Already promoted")).when(promoteUseCase).execute(uuid);
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid)).andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectIneligiblePromotion() throws Exception {
        doThrow(new InvalidUserRoleException("Invalid role")).when(promoteUseCase).execute(uuid);
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectInvalidUuid() throws Exception {
        mvc.perform(patch("/api/v1/teachers/promote/invalid/teacher")).andExpect(status().isBadRequest());
        verifyNoInteractions(promoteUseCase);
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "TEACHER"})
    void shouldForbidNonAdmins(String role) throws Exception {
        mvc.perform(post("/api/v1/teachers/create").with(user("pedro").roles(role))
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid).with(user("pedro").roles(role)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(createUseCase, promoteUseCase);
    }

    @Test
    void shouldRequireAuthentication() throws Exception {
        mvc.perform(post("/api/v1/teachers/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isUnauthorized());
        mvc.perform(patch("/api/v1/teachers/promote/{uuid}/teacher", uuid)).andExpect(status().isUnauthorized());
        verifyNoInteractions(createUseCase, promoteUseCase);
    }
}
