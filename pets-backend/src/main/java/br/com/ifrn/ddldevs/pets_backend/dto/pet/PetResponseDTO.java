package br.com.ifrn.ddldevs.pets_backend.dto.pet;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Date;

public record PetResponseDTO(
        @Schema(description = "Pet's ID", example = "1") Long id,
        @Schema(description = "Pet's name", example = "Apolo")String name,
        @Schema(description = "Pet's sex", example = "MALE")Gender gender,
        @Schema(description = "Pet's age", example = "2")Integer age,
        @Schema(description = "Pet's weight (kg)", example = "2.5")Double weight,
        @Schema(description = "Pet's breed", example = "Yorkshire")String breed,
        @Schema(description = "Pet's species", example = "Dog")String species,
        @Schema(description = "Pet's height (cm)", example = "30")Integer height,
        @Schema(description = "Pet's birthdate", example = "2024-12-05T14:30:00Z") LocalDate dateOfBirth,
        @Schema(description = "Pet's photo", example = "www.foto.com") String photoUrl
){}
