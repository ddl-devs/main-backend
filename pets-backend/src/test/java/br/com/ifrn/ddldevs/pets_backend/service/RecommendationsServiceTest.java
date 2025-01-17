package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.Recommendation;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.mapper.RecommendationMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private RecommendationMapper recommendationMapper;

    @InjectMocks
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRecommendationWithValidPet() {
        Pet pet = new Pet();
        pet.setId(1L);

        RecommendationRequestDTO requestDTO = new RecommendationRequestDTO(1L, "Feed your pet twice daily", "Nutrition", LocalDateTime.now());
        Recommendation recommendation = new Recommendation();
        RecommendationResponseDTO responseDTO = new RecommendationResponseDTO(1L, "Feed your pet twice daily", "Nutrition", LocalDateTime.now());

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(recommendationMapper.toEntity(requestDTO)).thenReturn(recommendation);
        when(recommendationRepository.save(recommendation)).thenReturn(recommendation);
        when(recommendationMapper.toRecommendationResponseDTO(recommendation)).thenReturn(responseDTO);

        RecommendationResponseDTO result = recommendationService.createRecommendation(requestDTO);

        assertNotNull(result);
        assertEquals("Feed your pet twice daily", result.recommendation());
        assertEquals("Nutrition", result.categorieRecommendation());

        verify(petRepository).findById(1L);
        verify(recommendationRepository).save(recommendation);
    }

    @Test
    void createRecommendationWithInvalidPet() {
        RecommendationRequestDTO requestDTO = new RecommendationRequestDTO(999L, "Feed your pet twice daily", "Nutrition", LocalDateTime.now());

        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recommendationService.createRecommendation(requestDTO));

        assertEquals("User not found", exception.getMessage());

        verify(recommendationRepository, never()).save(any(Recommendation.class));
    }

    @Test
    void deleteRecommendationWithValidId() {
        when(recommendationRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> recommendationService.deleteRecommendation(1L));

        verify(recommendationRepository).deleteById(1L);
    }

    @Test
    void deleteRecommendationWithInvalidId() {
        when(recommendationRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recommendationService.deleteRecommendation(999L));

        assertEquals("Recommendation not found", exception.getMessage());

        verify(recommendationRepository, never()).deleteById(anyLong());
    }

    @Test
    void getRecommendationWithValidId() {
        Recommendation recommendation = new Recommendation();
        recommendation.setId(1L);
        recommendation.setRecommendation("Feed your pet twice daily");
        recommendation.setCategorieRecommendation("Nutrition");
        recommendation.setData(LocalDateTime.now());

        RecommendationResponseDTO responseDTO = new RecommendationResponseDTO(1L, "Feed your pet twice daily", "Nutrition", recommendation.getData());

        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));
        when(recommendationMapper.toRecommendationResponseDTO(recommendation)).thenReturn(responseDTO);

        RecommendationResponseDTO result = recommendationService.getRecommendation(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Feed your pet twice daily", result.recommendation());
    }

    @Test
    void getRecommendationWithInvalidId() {
        when(recommendationRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recommendationService.getRecommendation(999L));

        assertEquals("Recommendation not found", exception.getMessage());
    }

    @Test
    void getAllRecommendationsByPetIdWithValidId() {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(new Recommendation());

        when(recommendationRepository.findAllByPetId(1L)).thenReturn(recommendations);
        when(recommendationMapper.toDTOList(recommendations)).thenReturn(new ArrayList<>());

        List<RecommendationResponseDTO> response = recommendationService.getAllByPetId(1L);

        assertNotNull(response);
        verify(recommendationRepository).findAllByPetId(1L);
    }

    @Test
    void getAllRecommendationsByPetIdWithInvalidId() {
        when(recommendationRepository.findAllByPetId(999L)).thenReturn(new ArrayList<>());

        List<RecommendationResponseDTO> response = recommendationService.getAllByPetId(999L);

        assertTrue(response.isEmpty());
    }
}
