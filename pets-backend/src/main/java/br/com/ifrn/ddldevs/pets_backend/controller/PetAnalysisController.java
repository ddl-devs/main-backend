package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.service.PetAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-analysis")
@RequiredArgsConstructor
@Tag(name="Pets Analysis", description = "API for Pets Analysis management")
public class PetAnalysisController {

    private final PetAnalysisService petAnalysisService;

    @PostMapping("/")
    @Operation(summary = "Create new Pet Analysis")
    public ResponseEntity<PetAnalysisResponseDTO> createPetAnalysis(@Valid @RequestBody PetAnalysisRequestDTO petAnalysisRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petAnalysisService.createPetAnalysis(petAnalysisRequestDTO));
    }

    @GetMapping("/")
    @Operation(summary = "List Pet Analyses")
    public ResponseEntity<List<PetAnalysisResponseDTO>> listPetAnalyses() {
        return ResponseEntity.status(HttpStatus.OK).body(petAnalysisService.listPetAnalyses());
    }

    @GetMapping( "/{id}")
    @Operation(summary = "Get Pet Analysis by id")
    public ResponseEntity<PetAnalysisResponseDTO> getPetAnalysis(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(petAnalysisService.getPetAnalysis(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Pet Analysis by id")
    public ResponseEntity<Void> deletePetAnalysis(@PathVariable Long id) {
        petAnalysisService.deletePetAnalysis(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/pet/{id}")
    @Operation(summary = "Get Pet Analyses by pet id")
    public ResponseEntity<List<PetAnalysisResponseDTO>> getPetAnalysisByPetId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(petAnalysisService.getAllByPetId(id));
    }
}