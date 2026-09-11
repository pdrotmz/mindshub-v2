package br.com.mindshub.identity.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginUserResponse(

        @Schema(description = "user login token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZWRyb3RvbWF6OTE4MUBnbWFpbC5jb20iLCJpYXQiOjE3ODc2NzUyNTMsImV4cCI6MTc4Nzc2MTY1M30.279YbZyUe0x-tTbdqySARcsyVPNFxwEi52l4JwBJ5vmXRYZvjWI981B0RUraDuFlzNYPWGYprNsuJM5SBexrXQ")
        String token
) {
}
