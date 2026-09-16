package br.com.mindshub.identity.presentation.controller.admin;

import br.com.mindshub.identity.application.usecase.admin.CreateAdminUseCase;
import br.com.mindshub.identity.application.usecase.admin.PromoteToAdminUseCase;
import br.com.mindshub.identity.presentation.dto.request.RegisterAdminRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Identity - Admin", description = "Endpoints to admins")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CreateAdminUseCase createAdminUseCase;
    private final PromoteToAdminUseCase promoteToAdminUseCase;

    @Operation(summary = "Creates an admin.", description = "An admin can create an admin.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creates a teacher in mindshub system."),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, teacher not created."),
            @ApiResponse(responseCode = "409", description = "Email has been registered.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterAdminRequest request) {
        createAdminUseCase.execute(request);
    }

    @Operation(summary = "Promotes an user to admin.", description = "An admin can promote any user to admin.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion successfully registered."),
            @ApiResponse(responseCode = "404", description = "User was not found.")
    })
    @PatchMapping("/promote/{uuid}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void promote(@PathVariable UUID uuid) {
        promoteToAdminUseCase.execute(uuid);
    }
}
