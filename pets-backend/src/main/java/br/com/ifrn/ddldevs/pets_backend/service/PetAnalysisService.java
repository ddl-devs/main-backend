package br.com.ifrn.ddldevs.pets_backend.service;


import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.PetAnalysis;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetAnalysisMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetAnalysisRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public PetAnalysisResponseDTO createPetAnalysis(PetAnalysisRequestDTO petAnalysisRequestDTO) {
        PetAnalysis petAnalysis = petAnalysisMapper.toEntity(petAnalysisRequestDTO);
        Pet pet = petRepository.findById(petAnalysisRequestDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

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

    public void deletePetAnalysis(Long id) {
        petAnalysisRepository.deleteById(id);
    }

    public List<PetAnalysisResponseDTO> getAllByPetId(Long id) {
        List<PetAnalysis> petAnalyses = petAnalysisRepository.findAllByPetId(id);
        return petAnalysisMapper.toResponseList(petAnalyses);
    }
    
    public PetAnalysisResponseDTO getPetAnalysis(Long analysisId) {
        PetAnalysis petAnalysis = petAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new EntityNotFoundException("Pet Analysis not found"));
        return petAnalysisMapper.toResponse(petAnalysis);
    }
}