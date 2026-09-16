package br.com.mindshub.identity.presentation.controller.user;

import br.com.mindshub.identity.application.usecase.user.*;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.presentation.dto.request.UpdateUserPasswordRequest;
import br.com.mindshub.identity.presentation.dto.request.UpdateUsersInfoRequest;
import br.com.mindshub.identity.presentation.dto.response.user.MeResponse;
import br.com.mindshub.identity.presentation.dto.response.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Identity - User", description = "Endpoints to users")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUsersByRoleUseCase getUsersByRoleUseCase;
    private final GetAllUsersByActiveAccount getAllUsersByActiveAccount;
    private final GetUserByUuidUseCase getUserByUuidUseCase;
    private final UpdateUsersInfoUseCase updateUsersInfoUseCase;
    private final UpdateUserPasswordUseCase updateUserPasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @Operation(summary = "Get user info", description = "Where user get their infos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
    })
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUserUseCase.execute(userDetails.getUsername());
        return ResponseEntity.ok(MeResponse.from(user));
    }

    @Operation(summary = "List all users", description = "List all users in mindshub system")
    @ApiResponse(responseCode = "200", description = "All users listed successfully.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> response = getAllUsersUseCase.execute()
                .stream().map(UserResponse::from).toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all users by user role", description = "List all users by user role in muindshub system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description ="All users listed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request occurred.")
})
    @GetMapping("by-role")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> findUsersByRole(@RequestParam("role") Role role) {
        List<UserResponse> response = getUsersByRoleUseCase.execute(role)
                .stream().map(UserResponse::from).toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all users by status account", description = "List all users by status account in muindshub system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description ="All users listed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request occurred.")
    })
    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> findUsersByUuid(@PathVariable UUID userId) {
        UserResponse response = UserResponse.from(getUserByUuidUseCase.execute(userId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user info by uuid", description = " Get user info by a specific uuid in mindshub system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description ="All users listed successfully."),
            @ApiResponse(responseCode = "404", description = "Bad request occurred.")
    })
    @GetMapping("status")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> findUsersByStatusAccount(@RequestParam("status") boolean status) {
        List<UserResponse> response = getAllUsersByActiveAccount.execute(status)
                .stream().map(UserResponse::from).toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user info", description = "Update user info in mindshub system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description ="User info updated successfully."),
            @ApiResponse(responseCode = "404", description = "User was not found.")
    })
    @PatchMapping("me/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateUsersInfoRequest request) {
        updateUsersInfoUseCase.execute(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update user password", description = "Update user password in mindshub system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description ="User password updated successfully."),
            @ApiResponse(responseCode = "404", description = "User was not found.")
    })
    @PatchMapping("me/update/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateUserPassword(@AuthenticationPrincipal UserDetails userDetails ,@Valid @RequestBody UpdateUserPasswordRequest request) {
        updateUserPasswordUseCase.execute(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a user", description = "Delete a user in mindshub system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description ="User successfully deleted."),
            @ApiResponse(responseCode = "404", description = "User was not found.")
    })
    @DeleteMapping("/{userId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        deleteUserUseCase.execute(userId);
        return ResponseEntity.noContent().build();
    }
}
