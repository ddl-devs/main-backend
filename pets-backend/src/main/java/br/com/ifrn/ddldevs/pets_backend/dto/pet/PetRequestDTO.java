package br.com.ifrn.ddldevs.pets_backend.dto.pet;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisições de Pets")
public class PetRequestDTO {

    @Schema(description = "Pet's name", example = "Apolo")
    @Valid
    @NotNull
    @NotBlank
    private String name;

    @Schema(description = "Pet's sex", example = "MALE")
    @Valid
    private Gender gender;

    @Schema(description = "Pet's age", example = "2")
    private Integer age;

    @Schema(description = "Pet's weight (kg)", example = "2.5")
    @Valid
    @Positive
    private Double weight;

    @Schema(description = "Pet's species")
    @Valid
    @NotNull(message = "Pet's species is required!")
    @NotBlank
    private String species;

    @Schema(description = "Pet's breed", example = "Yorkshire")
    @Valid
    @NotBlank
    private String breed;

    @Schema(description = "Pet's height (cm)", example = "30")
    @Valid
    @Positive
    private Integer height;

    @Schema(description = "Pet's birthdate", example = "2024-12-05T14:30:00Z")
    @Valid
    @PastOrPresent
    private LocalDate dateOfBirth;

    @Schema(description = "Pet's photo", example = "www.foto.com")
    @Valid
    @NotBlank
    private String photoUrl;

    @Schema(description = "Pet's owner id", example = "1")
    @Valid
    @NotNull(message = "Owner's id is mandatory")
    private Long userId;
}
