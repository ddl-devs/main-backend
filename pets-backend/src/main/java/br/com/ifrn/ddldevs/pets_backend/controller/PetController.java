package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pets")
@Tag(name="Pets", description = "API for Pets management")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "Create new pet")
    @PostMapping("/")
    public ResponseEntity<PetResponseDTO> createPet(@Valid @RequestBody PetRequestDTO petRequestDTO) {
        return ResponseEntity.ok(petService.createPet(petRequestDTO));
    }

    @Operation(summary = "List pets")
    @GetMapping("/")
    public ResponseEntity<List<PetResponseDTO>> listPets(){
        return ResponseEntity.ok(petService.listPets());
    }

    @Operation(summary = "Update a pet")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Long id, @RequestBody PetRequestDTO petRequestDTO) {
        return ResponseEntity.ok(petService.updatePet(id,petRequestDTO));
    }

    @Operation(summary = "Get pet by id")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPet(id));
    }

    @Operation(summary = "Delete a pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().build();
    }
}
