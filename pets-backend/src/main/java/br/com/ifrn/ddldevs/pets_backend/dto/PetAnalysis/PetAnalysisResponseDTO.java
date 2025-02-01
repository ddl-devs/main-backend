package br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PetAnalysisResponseDTO(
        @Schema(description = "Id of the Analysis", example = "1") Long id,
        @Schema(description = "Date and time of Analysis creation", example = "2024-12-05T14:30:00Z") LocalDateTime createdAt,
        @Schema(description = "Picture URL", example = "http://example.com/pet-analysis/picture.jpg") String picture,
        @Schema(description = "Result of the Analysis", example = "Healthy") String result,
        @Schema(description = "Type of the Analysis", example = "Blood Test") String analysisType
) {}