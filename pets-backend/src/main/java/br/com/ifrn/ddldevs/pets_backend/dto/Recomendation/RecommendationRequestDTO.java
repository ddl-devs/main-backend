package br.com.ifrn.ddldevs.pets_backend.dto.Recomendation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a request for a recommendation")
public class RecommendationRequestDTO {

    @Schema(description = "Id of the pet", example = "1")
    @NotNull(message = "Pet's id is mandatory")
    @Valid
    private Long petId;

    @Schema(description = "Details of the recommendation", example = "Feed your pet twice daily")
    @NotBlank
    private String recommendation;

    @Schema(description = "Category of the recommendation", example = "Nutrition")
    @NotBlank
    private String categorieRecommendation;

    @Schema(description = "Date and time of the recommendation", example = "2024-12-05T14:30:00Z")
    @NotNull
    private LocalDateTime timestamp;

}