package br.com.mindshub.identity.presentation.controller.user;
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

import br.com.mindshub.identity.application.usecase.user.*;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.infrastructure.security.jwt.JwtService;
import java.util.List;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTestSupport {
    @MockitoBean private GetCurrentUserUseCase currentUseCase;
    @MockitoBean private GetAllUsersUseCase allUseCase;
    @MockitoBean private GetUsersByRoleUseCase roleUseCase;
    @MockitoBean private GetAllUsersByActiveAccount activeUseCase;
    @MockitoBean private GetUserByUuidUseCase uuidUseCase;
    @MockitoBean private UpdateUsersInfoUseCase updateUseCase;
    @MockitoBean private UpdateUserPasswordUseCase passwordUseCase;
    @MockitoBean private DeleteUserUseCase deleteUseCase;
    private final UUID uuid = UUID.randomUUID();

    @Test
    @WithMockUser(username = "pedro@example.com", roles = "STUDENT")
    void shouldReturnCurrentUserWithoutPassword() throws Exception {
        when(currentUseCase.execute("pedro@example.com")).thenReturn(existingUser());
        mvc.perform(get("/api/v1/users/me")).andExpect(status().isOk())
                .andExpect(jsonPath("email").value("pedro@example.com"))
                .andExpect(jsonPath("username").value("pedro"))
                .andExpect(jsonPath("role").value("STUDENT"))
                .andExpect(jsonPath("password").doesNotExist());
        verify(currentUseCase).execute("pedro@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListUsersWithoutPasswords() throws Exception {
        when(allUseCase.execute()).thenReturn(List.of(existingUser()));
        mvc.perform(get("/api/v1/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(uuid.toString()))
                .andExpect(jsonPath("$[0].email").value("pedro@example.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
        verify(allUseCase).execute();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFilterByRole() throws Exception {
        when(roleUseCase.execute(Role.STUDENT)).thenReturn(List.of(existingUser()));
        mvc.perform(get("/api/v1/users/by-role").param("role", "STUDENT")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("STUDENT"));
        verify(roleUseCase).execute(Role.STUDENT);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser(roles = "ADMIN")
    void shouldFilterByAccountStatus(boolean active) throws Exception {
        User user = existingUser();
        user.setActive(active);
        when(activeUseCase.execute(active)).thenReturn(List.of(user));
        mvc.perform(get("/api/v1/users/status").param("status", String.valueOf(active))).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isActive").value(active));
        verify(activeUseCase).execute(active);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindUserByUuid() throws Exception {
        when(uuidUseCase.execute(uuid)).thenReturn(existingUser());
        mvc.perform(get("/api/v1/users/{uuid}", uuid)).andExpect(status().isOk())
                .andExpect(jsonPath("uuid").value(uuid.toString()));
        verify(uuidUseCase).execute(uuid);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFound() throws Exception {
        when(uuidUseCase.execute(uuid)).thenThrow(new UserNotFoundException("User not found"));
        mvc.perform(get("/api/v1/users/{uuid}", uuid)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "pedro@example.com", roles = "STUDENT")
    void shouldUpdateAuthenticatedUserInfo() throws Exception {
        var request = new UpdateUsersInfoRequest("new-name", "new@example.com");
        mvc.perform(patch("/api/v1/users/me/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNoContent());
        verify(updateUseCase).execute("pedro@example.com", request);
    }

    @Test
    @WithMockUser(username = "pedro@example.com", roles = "STUDENT")
    void shouldUpdateAuthenticatedUserPassword() throws Exception {
        var request = new UpdateUserPasswordRequest("Old@1234", "New@1234", "New@1234");
        mvc.perform(patch("/api/v1/users/me/update/password").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNoContent());
        verify(passwordUseCase).execute("pedro@example.com", request);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldRejectMismatchedPasswordConfirmation() throws Exception {
        var request = new UpdateUserPasswordRequest("Old@1234", "New@1234", "Wrong@1234");
        mvc.perform(patch("/api/v1/users/me/update/password").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
        verifyNoInteractions(passwordUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/v1/users/{uuid}/delete", uuid)).andExpect(status().isNoContent());
        verify(deleteUseCase).execute(uuid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "TEACHER"})
    void shouldForbidAdministrativeEndpoints(String role) throws Exception {
        for (String path : List.of("", "/by-role?role=STUDENT", "/status?status=true", "/" + uuid)) {
            mvc.perform(get("/api/v1/users" + path).with(user("pedro").roles(role)))
                    .andExpect(status().isForbidden());
        }
        mvc.perform(delete("/api/v1/users/{uuid}/delete", uuid).with(user("pedro").roles(role)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(allUseCase, roleUseCase, activeUseCase, uuidUseCase, deleteUseCase);
    }

    @Test
    void shouldRequireAuthenticationOnEveryEndpoint() throws Exception {
        for (String path : List.of("", "/me", "/by-role?role=STUDENT", "/status?status=true", "/" + uuid)) {
            mvc.perform(get("/api/v1/users" + path)).andExpect(status().isUnauthorized());
        }
        mvc.perform(delete("/api/v1/users/{uuid}/delete", uuid)).andExpect(status().isUnauthorized());
        mvc.perform(patch("/api/v1/users/me/update").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(patch("/api/v1/users/me/update/password").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(currentUseCase, allUseCase, roleUseCase, activeUseCase, uuidUseCase,
                deleteUseCase, updateUseCase, passwordUseCase);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/by-role?role=INVALID", "/status?status=invalid", "/invalid"})
    @WithMockUser(roles = "ADMIN")
    void shouldRejectInvalidParameters(String path) throws Exception {
        mvc.perform(get("/api/v1/users" + path)).andExpect(status().isBadRequest());
        verifyNoInteractions(roleUseCase, activeUseCase, uuidUseCase);
    }

    @Test
    void shouldAuthenticateRealSignedJwt() throws Exception {
        var principal = org.springframework.security.core.userdetails.User.withUsername("pedro@example.com")
                .password("encoded").roles("STUDENT").build();
        when(userDetailsService.loadUserByUsername(principal.getUsername())).thenReturn(principal);
        when(currentUseCase.execute(principal.getUsername())).thenReturn(existingUser());
        mvc.perform(get("/api/v1/users/me").header("Authorization", "Bearer " + jwtService.generateToken(principal)))
                .andExpect(status().isOk()).andExpect(jsonPath("email").value(principal.getUsername()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-token", ""})
    void shouldRejectMalformedJwt(String token) throws Exception {
        mvc.perform(get("/api/v1/users/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(currentUseCase);
    }

    @Test
    @WithMockUser(username = "pedro@example.com", roles = "STUDENT")
    void shouldReturnConflictForTakenUsername() throws Exception {
        var request = new UpdateUsersInfoRequest("taken", "pedro@example.com");
        doThrow(new UsernameAlreadyRegisteredException("Username already registered."))
                .when(updateUseCase).execute("pedro@example.com", request);
        mvc.perform(patch("/api/v1/users/me/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("status").value(409))
                .andExpect(jsonPath("error").value("Conflict"))
                .andExpect(jsonPath("message").value("Username already registered."))
                .andExpect(jsonPath("path").value("/api/v1/users/me/update"))
                .andExpect(jsonPath("timestamp").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "pedro@example.com", roles = "STUDENT")
    void shouldReturnBadRequestForIncorrectCurrentPassword() throws Exception {
        var request = new UpdateUserPasswordRequest("Wrong@123", "New@1234", "New@1234");
        doThrow(new InvalidCurrentPasswordException("Current password is incorrect."))
                .when(passwordUseCase).execute("pedro@example.com", request);
        mvc.perform(patch("/api/v1/users/me/update/password").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Current password is incorrect."))
                .andExpect(jsonPath("path").value("/api/v1/users/me/update/password"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnConflictForAlreadyDeletedUser() throws Exception {
        doThrow(new UserAlreadyDeletedException("User is already deleted."))
                .when(deleteUseCase).execute(uuid);
        mvc.perform(delete("/api/v1/users/{uuid}/delete", uuid))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("User is already deleted."))
                .andExpect(jsonPath("path").value("/api/v1/users/" + uuid + "/delete"));
    }

    private User existingUser() {
        User user = new User();
        user.setUuid(uuid);
        user.setUsername("pedro");
        user.setEmail("pedro@example.com");
        user.setPassword("never-expose-this");
        user.setRole(Role.STUDENT);
        user.setActive(true);
        return user;
    }
}
