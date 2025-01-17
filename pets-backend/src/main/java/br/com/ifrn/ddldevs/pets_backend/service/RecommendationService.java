package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.controller.RecommendationController;
import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.Recommendation;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationResponseDTO;

import br.com.ifrn.ddldevs.pets_backend.mapper.RecommendationMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public RecommendationResponseDTO createRecommendation(RecommendationRequestDTO recommendationRequestDTO) {
        Recommendation recommendation = recommendationMapper.toEntity(recommendationRequestDTO);
        Pet pet = petRepository.findById(recommendationRequestDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        recommendation.setPet(pet);
        pet.getRecommendations().add(recommendation);

        recommendationRepository.save(recommendation);
        petRepository.save(pet);

        return recommendationMapper.toRecommendationResponseDTO(recommendation);
    }

    public List<RecommendationResponseDTO> listRecommendations() {
        List<Recommendation> recommendations = recommendationRepository.findAll();
        return recommendationMapper.toDTOList(recommendations);
    }

    public RecommendationResponseDTO getRecommendation(Long id) {
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        return recommendationMapper.toRecommendationResponseDTO(recommendation);
    }

    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    public List<RecommendationResponseDTO> getAllByPetId(Long id) {
        List<Recommendation> recommendations = recommendationRepository.findAllByPetId(id);
        return recommendationMapper.toDTOList(recommendations);
    }

    public List<RecommendationResponseDTO> getAllByUserId(Long id) {
        List<Recommendation> recommendations = recommendationRepository.findAllByUserId(id);
        return recommendationMapper.toDTOList(recommendations);
    }
}