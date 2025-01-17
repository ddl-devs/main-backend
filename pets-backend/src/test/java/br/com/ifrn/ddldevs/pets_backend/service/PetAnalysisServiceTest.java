package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.PetAnalysis;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetAnalysisMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetAnalysisRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetAnalysisServiceTest {

    @Mock
    private PetAnalysisRepository petAnalysisRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetAnalysisMapper petAnalysisMapper;

    @InjectMocks
    private PetAnalysisService petAnalysisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPetAnalysisWithValidPet() {
        Pet pet = new Pet();
        pet.setId(1L);

        PetAnalysisRequestDTO requestDTO = new PetAnalysisRequestDTO(1L, "http://example.com/picture.jpg", "Healthy", "Blood Test");
        PetAnalysis petAnalysis = new PetAnalysis();
        PetAnalysisResponseDTO responseDTO = new PetAnalysisResponseDTO(1L, "http://example.com/picture.jpg", "Healthy", "Blood Test");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petAnalysisMapper.toEntity(requestDTO)).thenReturn(petAnalysis);
        when(petAnalysisRepository.save(petAnalysis)).thenReturn(petAnalysis);
        when(petAnalysisMapper.toResponse(petAnalysis)).thenReturn(responseDTO);

        PetAnalysisResponseDTO result = petAnalysisService.createPetAnalysis(requestDTO);

        assertNotNull(result);
        assertEquals("http://example.com/picture.jpg", result.picture());
        assertEquals("Healthy", result.result());
        assertEquals("Blood Test", result.analysisType());

        verify(petRepository).findById(1L);
        verify(petAnalysisRepository).save(petAnalysis);
    }

    @Test
    void createPetAnalysisWithInvalidPet() {
        PetAnalysisRequestDTO requestDTO = new PetAnalysisRequestDTO(999L, "http://example.com/picture.jpg", "Healthy", "Blood Test");

        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> petAnalysisService.createPetAnalysis(requestDTO));

        assertEquals("Pet not found", exception.getMessage());

        verify(petAnalysisRepository, never()).save(any(PetAnalysis.class));
    }

    @Test
    void deletePetAnalysisWithValidId() {
        when(petAnalysisRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> petAnalysisService.deletePetAnalysis(1L));

        verify(petAnalysisRepository).deleteById(1L);
    }

    @Test
    void deletePetAnalysisWithInvalidId() {
        when(petAnalysisRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> petAnalysisService.deletePetAnalysis(999L));

        assertEquals("Pet Analysis not found", exception.getMessage());

        verify(petAnalysisRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllByUserIdWithValidId() {
        List<PetAnalysis> analyses = new ArrayList<>();
        analyses.add(new PetAnalysis());

        when(petAnalysisRepository.findAllByUserId(1L)).thenReturn(analyses);
        when(petAnalysisMapper.toResponseList(analyses)).thenReturn(new ArrayList<>());

        List<PetAnalysisResponseDTO> response = petAnalysisService.getAllByUserId(1L);

        assertNotNull(response);
        verify(petAnalysisRepository).findAllByUserId(1L);
    }

    @Test
    void getAllByUserIdWithInvalidId() {
        when(petAnalysisRepository.findAllByUserId(999L)).thenReturn(new ArrayList<>());

        List<PetAnalysisResponseDTO> response = petAnalysisService.getAllByUserId(999L);

        assertTrue(response.isEmpty());
    }

    @Test
    void getPetAnalysesByPetIdWithValidId() {
        List<PetAnalysis> analyses = new ArrayList<>();
        analyses.add(new PetAnalysis());

        when(petAnalysisRepository.findAllByPetId(1L)).thenReturn(analyses);
        when(petAnalysisMapper.toResponseList(analyses)).thenReturn(new ArrayList<>());

        List<PetAnalysisResponseDTO> response = petAnalysisService.getAllByPetId(1L);

        assertNotNull(response);
        verify(petAnalysisRepository).findAllByPetId(1L);
    }

    @Test
    void getPetAnalysesByPetIdWithInvalidId() {
        when(petAnalysisRepository.findAllByPetId(999L)).thenReturn(new ArrayList<>());

        List<PetAnalysisResponseDTO> response = petAnalysisService.getAllByPetId(999L);

        assertTrue(response.isEmpty());
    }
}