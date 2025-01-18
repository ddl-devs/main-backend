package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.Recommendation;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.mapper.RecommendationMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class RecommendationsServiceTest {

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
    void createRecommendationWithInvalidIdPet() {
        RecommendationRequestDTO requestDTO = new RecommendationRequestDTO(-1L, "Feed your pet twice daily", "Nutrition", LocalDateTime.now());

        when(petRepository.findById(-1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recommendationService.createRecommendation(requestDTO));

        assertEquals("ID não pode ser negativo", exception.getMessage());

        verify(recommendationRepository, never()).save(any(Recommendation.class));
    }

    @Test
    void createRecommendationWithNullIDPet() {
        RecommendationRequestDTO requestDTO = new RecommendationRequestDTO(null, "Feed your pet twice daily", "Nutrition", LocalDateTime.now());

        when(petRepository.findById(null)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recommendationService.createRecommendation(requestDTO));

        assertEquals("ID não pode ser nulo", exception.getMessage());

        verify(recommendationRepository, never()).save(any(Recommendation.class));
    }

    // b

    @Test
    void deleteRecommendationWithValidId() {
        when(recommendationRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> recommendationService.deleteRecommendation(1L));

        verify(recommendationRepository).deleteById(1L);
    }

    @Test
    void deleteRecommendationWithIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.deleteRecommendation(null),
                "ID não pode ser nulo");
    }

    @Test
    void deleteRecommendationWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.deleteRecommendation(-1L),
                "ID não pode ser negativo");
    }

    // c

    @Test
    void getRecommendationByUserWithValidId() {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(new Recommendation());

        when(recommendationRepository.findAllByUserId(1L)).thenReturn(recommendations);
        when(recommendationMapper.toDTOList(recommendations)).thenReturn(new ArrayList<>());

        List<RecommendationResponseDTO> response = recommendationService.getAllByUserId(1L);

        assertNotNull(response);
        verify(recommendationRepository).findAllByUserId(1L);

    }

    @Test
    void getRecommendationByUserWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getAllByUserId(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getRecommendationByUserWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getAllByUserId(null),
                "ID não pode ser nulo");
    }

    // d

    @Test
    void getRecommendationsByPetIdWithValidId() {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(new Recommendation());

        when(recommendationRepository.findAllByPetId(1L)).thenReturn(recommendations);
        when(recommendationMapper.toDTOList(recommendations)).thenReturn(new ArrayList<>());

        List<RecommendationResponseDTO> response = recommendationService.getAllByPetId(1L);

        assertNotNull(response);
        verify(recommendationRepository).findAllByPetId(1L);
    }

    @Test
    void getRecommendationByPetWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getAllByPetId(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getRecommendationByPetWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getAllByPetId(null),
                "ID não pode ser nulo");
    }

    // e

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
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getRecommendation(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getRecommentationWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> recommendationService.getRecommendation(null),
                "ID não pode ser nulo");
    }

}
