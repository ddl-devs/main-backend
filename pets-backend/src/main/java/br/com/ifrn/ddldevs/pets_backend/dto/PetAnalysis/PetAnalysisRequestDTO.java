package br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a request for Pet Analysis")
public class PetAnalysisRequestDTO {
    @Schema(description = "Id of the Pet", example = "1")
    @NotNull
    @Valid
    private Long petId;

    @NotNull
    @Schema(description = "Picture URL", example = "http://example.com/pet-analysis/picture.jpg")
    private String picture;

    @NotNull
    @Schema(description = "Result of the Analysis", example = "Healthy")
    private String result;

    @NotNull
    @Schema(description = "Type of the Analysis", example = "Blood Test")
    private String analysisType;

}