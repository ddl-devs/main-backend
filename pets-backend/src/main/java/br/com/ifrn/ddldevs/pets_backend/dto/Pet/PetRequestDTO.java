package br.com.ifrn.ddldevs.pets_backend.dto.Pet;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisições de Pets")
public class PetRequestDTO {

    @Schema(description = "Pet's name", example = "Apolo")
    private String name;

    @Schema(description = "Pet's sex", example = "MALE")
    private Gender gender;

    @Schema(description = "Pet's age", example = "2")
    private Integer age;

    @Schema(description = "Pet's weight (kg)", example = "2.5")
    private Double weight;

    @Schema(description = "Pet's breed", example = "Yorkshire")
    private String breed;

    @Schema(description = "Pet's height (cm)", example = "30")
    private Integer height;

    @Schema(description = "Pet's birthdate", example = "2024-12-05T14:30:00Z")
    private Date dateOfBirth;

    @Schema(description = "Pet's photo", example = "www.foto.com")
    private String photoUrl;

    @Schema(description = "Pet's owner id", example = "1")
    @NotNull(message = "Owner's id is mandatory")
    @Valid
    private Long userId;
}
