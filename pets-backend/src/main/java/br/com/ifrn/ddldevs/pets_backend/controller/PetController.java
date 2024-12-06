package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;

    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetRequestDTO petRequestDTO) {
        return ResponseEntity.ok(petService.createPet(petRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> listPets(){
        return ResponseEntity.ok(petService.listPets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Long id, @RequestBody PetRequestDTO petRequestDTO) {
        return ResponseEntity.ok(petService.updatePet(id,petRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPet(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().build();
    }
}
