package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.service.PetAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public PetAnalysisResponseDTO createPetAnalysis(@Valid @RequestBody PetAnalysisRequestDTO petAnalysisRequestDTO) {
        return petAnalysisService.createPetAnalysis(petAnalysisRequestDTO);
    }

    @GetMapping("/")
    @Operation(summary = "List Pet Analyses")
    public List<PetAnalysisResponseDTO> listPetAnalyses() {
        return petAnalysisService.listPetAnalyses();
    }

    @GetMapping( "/{id}")
    @Operation(summary = "Get Pet Analysis by id")
    public PetAnalysisResponseDTO getPetAnalysis(@PathVariable Long id) {
        return petAnalysisService.getPetAnalysis(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Pet Analysis by id")
    public void deletePetAnalysis(@PathVariable Long id) {
        petAnalysisService.deletePetAnalysis(id);
    }

    @GetMapping("/pet/{id}")
    @Operation(summary = "Get Pet Analyses by pet id")
    public List<PetAnalysisResponseDTO> getPetAnalysisByPetId(@PathVariable Long id) {
        return petAnalysisService.getAllByPetId(id);
    }
}