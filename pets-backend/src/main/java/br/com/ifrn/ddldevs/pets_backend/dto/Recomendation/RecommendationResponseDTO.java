package br.com.ifrn.ddldevs.pets_backend.dto.Recomendation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RecommendationResponseDTO(
        @Schema(description = "Recommendation's ID", example = "1") Long id,
        @Schema(description = "Details of the recommendation", example = "Feed your pet twice daily") String recommendation,
        @Schema(description = "Category of the recommendation", example = "Nutrition") String categorieRecommendation,
        @Schema(description = "Date and time of the recommendation", example = "2024-12-05T14:30:00Z") LocalDateTime data
) {}