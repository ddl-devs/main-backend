package br.com.ifrn.ddldevs.pets_backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

// keycloakId is temporary
public record UserRequestDTO(
        @Schema(description = "User's name", example = "user123") String username,
        @Schema(description = "User's keycloak id", example = "345") String keycloakId,
        @Schema(description = "User's email", example = "user@gmail.com") String email,
        @Schema(description = "User's firstname", example = "user") String firstName,
        @Schema(description = "User's lastname", example = "silva")String lastName,
        @Valid @PastOrPresent @Schema(description = "User's " +
                "birthdate", example = "2024-12-05T14:30:00Z")Date dateOfBirth,
        @Schema(description = "User's profile photo url", example = "aws.12bs.bucket.com")String photoUrl,
        @Schema(description = "User's password", example = "test123")String password
){}
