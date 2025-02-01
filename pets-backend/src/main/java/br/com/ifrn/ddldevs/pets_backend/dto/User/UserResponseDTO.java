package br.com.ifrn.ddldevs.pets_backend.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(
        @Schema(description = "User's ID", example = "12") Long id,
        @Schema(description = "Date and time of User creation", example = "2024-12-05T14:30:00Z") LocalDateTime createdAt,
        @Schema(description = "Date and time the User was last updated", example = "2024-12-05T14:30:00Z") LocalDateTime updatedAt,
        @Schema(description = "User's name", example = "user123") String username,
        @Schema(description = "User's keycloak id", example = "345") String keycloakId,
        @Schema(description = "User's email", example = "user@gmail.com") String email,
        @Schema(description = "User's firstname", example = "user") String firstName,
        @Schema(description = "User's lastname", example = "silva")String lastName,
        @Schema(description = "User's birthdate", example = "2024-12-05T14:30" +
                ":00Z") LocalDate dateOfBirth,
        @Schema(description = "User's profile photo url", example = "aws.12bs.bucket.com")String photoUrl
){}
