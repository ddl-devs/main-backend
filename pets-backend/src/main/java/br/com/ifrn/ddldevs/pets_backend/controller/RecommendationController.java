package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@Tag(name="Recommendations", description = "API for Pet Recommendations management")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/")
    @Operation(summary = "Create new recommendation")
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@Valid @RequestBody RecommendationRequestDTO recommendationRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recommendationService.createRecommendation(recommendationRequestDTO));
    }

    @GetMapping("/")
    @Operation(summary = "List recommendations")
    public List<RecommendationResponseDTO> listRecommendations() {
        return recommendationService.listRecommendations();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recommendation by id")
    public RecommendationResponseDTO getRecommendation(@PathVariable Long id) {
        return recommendationService.getRecommendation(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recommendation by id")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("pet/{id}")
    @Operation(summary = "Get recommendations by pet id")
    public List<RecommendationResponseDTO> getRecommendationsByPetId(@PathVariable Long id) {
        return recommendationService.getAllByPetId(id);
    }
}