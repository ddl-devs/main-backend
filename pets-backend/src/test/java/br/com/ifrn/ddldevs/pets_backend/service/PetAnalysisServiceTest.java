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
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
        PetAnalysisRequestDTO requestDTO = new PetAnalysisRequestDTO(-1L, "http://example.com/picture.jpg", "Healthy", "Blood Test");

        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> petAnalysisService.createPetAnalysis(requestDTO));

        assertEquals("ID não pode ser negativo", exception.getMessage());

        verify(petAnalysisRepository, never()).save(any(PetAnalysis.class));
    }

    @Test
    void createPetAnalysisWithNullPet() {
        PetAnalysisRequestDTO requestDTO = new PetAnalysisRequestDTO(-1L, "http://example.com/picture.jpg", "Healthy", "Blood Test");

        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.createPetAnalysis(requestDTO),
                "ID não pode ser nulo");
    }

    // b

    @Test
    void deletePetAnalysisWithValidId() {
        when(petAnalysisRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> petAnalysisService.deletePetAnalysis(1L));

        verify(petAnalysisRepository).deleteById(1L);
    }

    @Test
    void deletePetAnalysisWithIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.deletePetAnalysis(null),
                "ID não pode ser nulo");
    }

    @Test
    void deletePetWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.deletePetAnalysis(-1L),
                "ID não pode ser negativo");
    }

    // c

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
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getAllByUserId(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getAllByUserIdWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getAllByUserId(null),
                "ID não pode ser nulo");
    }

    // d

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
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getAllByPetId(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getPetAnalysesByPetIdWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getAllByPetId(null),
                "ID não pode ser nulo");
    }

    // e

//    @Test
//    void getPetAnalysesWithValidId() {
//        // só faltou esse aqui
//    }

    @Test
    void getPetAnalysesWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getPetAnalysis(-1L),
                "ID não pode ser negativo");
    }

    @Test
    void getPetAnalysesIdWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> petAnalysisService.getAllByPetId(null),
                "ID não pode ser nulo");
    }
}