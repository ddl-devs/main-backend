package br.com.ifrn.ddldevs.pets_backend.service;


import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.PetAnalysis;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.AccessDeniedException;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetAnalysisMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetAnalysisRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetAnalysisService {

    @Autowired
    private PetAnalysisRepository petAnalysisRepository;

    @Autowired
    private PetAnalysisMapper petAnalysisMapper;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public PetAnalysisResponseDTO createPetAnalysis(
        PetAnalysisRequestDTO petAnalysisRequestDTO,
        String loggedUserKeycloakId
    ) {
        Long idPet = petAnalysisRequestDTO.getPetId();
        if (idPet == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (idPet < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        PetAnalysis petAnalysis = petAnalysisMapper.toEntity(petAnalysisRequestDTO);
        Pet pet = petRepository.findById(petAnalysisRequestDTO.getPetId())
            .orElseThrow(() -> new RuntimeException("Pet not found"));

        throwErrorIfPetBelongsToAnotherUser(pet, loggedUserKeycloakId);

        petAnalysis.setPet(pet);
        pet.getPetAnalysis().add(petAnalysis);

        petAnalysisRepository.save(petAnalysis);
        petRepository.save(pet);

        return petAnalysisMapper.toResponse(petAnalysis);
    }

    public List<PetAnalysisResponseDTO> listPetAnalyses() {
        List<PetAnalysis> petAnalyses = petAnalysisRepository.findAll();
        return petAnalysisMapper.toResponseList(petAnalyses);
    }

    public void deletePetAnalysis(Long id, String loggedUserKeycloakId) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        PetAnalysis petAnalysis = petAnalysisRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Análise não encontrada");
        });

        throwErrorIfPetBelongsToAnotherUser(petAnalysis.getPet(), loggedUserKeycloakId);

        petAnalysisRepository.deleteById(id);
    }

    public List<PetAnalysisResponseDTO> getAllByPetId(Long id, String loggedUserKeycloakId) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        List<PetAnalysis> petAnalyses = petAnalysisRepository.findAllByPetId(id);

        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));

        throwErrorIfPetBelongsToAnotherUser(pet, loggedUserKeycloakId);

        return petAnalysisMapper.toResponseList(petAnalyses);
    }

    public PetAnalysisResponseDTO getPetAnalysis(Long analysisId, String loggedUserKeycloakId) {
        if (analysisId == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (analysisId < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }
        PetAnalysis petAnalysis = petAnalysisRepository.findById(analysisId)
            .orElseThrow(() -> new EntityNotFoundException("Pet Analysis not found"));

        throwErrorIfPetBelongsToAnotherUser(petAnalysis.getPet(), loggedUserKeycloakId);

        return petAnalysisMapper.toResponse(petAnalysis);
    }

    private void throwErrorIfPetBelongsToAnotherUser(Pet pet, String loggedUserKeycloakId) {
        if (!pet.getUser().getKeycloakId().equals(loggedUserKeycloakId)) {
            throw new AccessDeniedException(
                "Você não pode acessar dados de pets de outros usuários!");
        }
    }
}