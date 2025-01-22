package br.com.ifrn.ddldevs.pets_backend.dto.user;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import br.com.ifrn.ddldevs.pets_backend.validator.MinAge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

// keycloakId is temporary
public record UserRequestDTO(
        @NotNull @Length(max=255) @Schema(description = "User's name",
                example = "user123") String username,
        @Schema(description = "User's keycloak id", example = "345") String keycloakId,
        @NotNull @Email @Schema(description = "User's email", example = "user" +
                "@gmail" +
                ".com") String email,
        @NotNull @Length(max=255) @Schema(description = "User's firstname", example = "user") String firstName,
        @NotNull @Length(max=255) @Schema(description = "User's lastname", example = "silva")String lastName,
        @MinAge @Schema(description = "User's " +
                "birthdate", example = "2024-12-05T14:30:00Z") LocalDate dateOfBirth,
        @Schema(description = "User's profile photo url", example = "aws.12bs.bucket.com")String photoUrl,
        @NotNull @Schema(description = "User's password",
                example = "test123")String password
){}
