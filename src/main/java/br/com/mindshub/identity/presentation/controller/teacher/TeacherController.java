package br.com.mindshub.identity.presentation.controller.teacher;

import br.com.mindshub.identity.application.usecase.teacher.CreateTeacherUseCase;
import br.com.mindshub.identity.application.usecase.teacher.PromoteToTeacherUseCase;
import br.com.mindshub.identity.presentation.dto.request.RegisterTeacherRequest;
import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
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

@Tag(name = "Identity - Teacher", description = "Endpoints to teachers")
@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final CreateTeacherUseCase createTeacherUseCase;
    private final PromoteToTeacherUseCase promoteToTeacherUseCase;

    @Operation(summary = "Creates a teacher.", description = "An admin can create a teacher.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creates a teacher in mindshub system."),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, teacher not created."),
            @ApiResponse(responseCode = "409", description = "Email has been registered.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterTeacherRequest request) {
        createTeacherUseCase.execute(request);
    }

    @Operation(summary = "Promotes an user to teacher.", description = "An admin can promote any user to teacher.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion successfully registered."),
            @ApiResponse(responseCode = "404", description = "User was not found.")
    })
    @PatchMapping("/promote/{uuid}/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void promote(@PathVariable UUID uuid) {
        promoteToTeacherUseCase.execute(uuid);
    }
}
