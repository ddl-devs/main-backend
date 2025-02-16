package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Species;
import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetUpdateRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class PetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    private Validator validator;

    public PetServiceTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPetValidInformation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("jhon");
        user.setFirstName("Jhon");
        user.setEmail("jhon@gmail.com");
        user.setKeycloakId("345");
        user.setPets(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        PetRequestDTO dto = new PetRequestDTO();
        dto.setUserId(user.getId());
        dto.setName("Apolo");
        dto.setSpecies(Species.DOG);
        dto.setHeight(30);
        dto.setWeight(BigDecimal.valueOf(10.0));

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setHeight(dto.getHeight());
        pet.setWeight(dto.getWeight());
        pet.setUser(user);

        PetResponseDTO petResponse = new PetResponseDTO(
                pet.getId(),
                pet.getCreatedAt(),
                pet.getUpdatedAt(),
                pet.getName(),
                pet.getGender(),
                pet.getAge(),
                pet.getWeight(),
                pet.getBreed(),
                pet.getSpecies(),
                pet.getHeight(),
                pet.getPhotoUrl()
        );

        when(petMapper.toEntity(dto)).thenReturn(pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.toPetResponseDTO(pet)).thenReturn(petResponse);

        PetResponseDTO response = petService.createPet(dto);

        assertNotNull(response);
        assertEquals("Apolo", response.name());
        assertEquals(Species.DOG, response.species());
        assertEquals(30, response.height());
        assertEquals(BigDecimal.valueOf(10.0), response.weight());

        verify(userRepository).findById(1L);
        verify(petMapper).toEntity(dto);
        verify(petRepository).save(any(Pet.class));
        verify(petMapper).toPetResponseDTO(pet);
    }

    @Test
    void createPetInvalidInformation() {
        PetRequestDTO dto = new PetRequestDTO();
        dto.setName("");
        dto.setWeight(BigDecimal.valueOf(-5.0));
        dto.setSpecies(Species.DOG);
        dto.setBreed("");
        dto.setHeight(0);
        dto.setUserId(null);

        Set<ConstraintViolation<PetRequestDTO>> violations = validator.validate(dto);
        System.out.println(violations);
        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
    }

    @Test
    void createWithInvalidGenderType() {
        ObjectMapper objectMapper = new ObjectMapper();
        String invalidJson = """
            {
                "name": "Buddy",
                "gender": "INVALID",
                "weight": 10.0,
                "species": "Dog",
                "breed": "Golden Retriever",
                "height": 50,
                "userId": 1
            }
        """;

        InvalidFormatException exception = assertThrows(
                InvalidFormatException.class,
                () -> objectMapper.readValue(invalidJson, PetRequestDTO.class)
        );
        assertTrue(exception.getMessage().contains("not one of the values accepted for Enum class: [FEMALE, MALE]"));
    }

    @Test
    void createPetNonExistentUser() {
        PetRequestDTO dto = new PetRequestDTO();
        dto.setName("Buddy");
        dto.setWeight(BigDecimal.valueOf(10.0));
        dto.setSpecies(Species.DOG);
        dto.setBreed("Golden Retriever");
        dto.setHeight(50);
        dto.setUserId(999L);

        when(userRepository.findById(dto.getUserId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.createPet(dto)
        );

        assertEquals("Usuário não existe", exception.getMessage());

        verify(petRepository, never()).save(any(Pet.class));
    }

    // b

    @Test
    void updatePetSuccessful() {
        Long petId = 1L;

        Pet existingPet = new Pet();
        existingPet.setId(petId);
        existingPet.setName("Buddy");
        existingPet.setSpecies(Species.CAT);
        existingPet.setHeight(20);
        existingPet.setWeight(BigDecimal.valueOf(5.0));

        PetUpdateRequestDTO updatedDTO = new PetUpdateRequestDTO();
        updatedDTO.setName("Apolo");
        updatedDTO.setSpecies(Species.DOG);
        updatedDTO.setHeight(30);
        updatedDTO.setWeight(BigDecimal.valueOf(10.0));

        Pet updatedPet = new Pet();
        updatedPet.setId(petId);
        updatedPet.setName("Apolo");
        updatedPet.setSpecies(Species.DOG);
        updatedPet.setHeight(30);
        updatedPet.setWeight(BigDecimal.valueOf(10.0));

        PetResponseDTO expectedResponse = new PetResponseDTO(
                petId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Apolo",
                null,
                null,
                BigDecimal.valueOf(10.0),
                null,
                Species.DOG,
                30,
                null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doAnswer(invocation -> {
            PetUpdateRequestDTO dto = invocation.getArgument(0);
            Pet pet = invocation.getArgument(1);
            pet.setName(dto.getName());
            pet.setSpecies(dto.getSpecies());
            pet.setHeight(dto.getHeight());
            pet.setWeight(dto.getWeight());
            return null;
        }).when(petMapper).updateEntityFromDTO(updatedDTO, existingPet);
        when(petRepository.save(existingPet)).thenReturn(updatedPet);
        when(petMapper.toPetResponseDTO(updatedPet)).thenReturn(expectedResponse);

        PetResponseDTO response = petService.updatePet(petId, updatedDTO);

        assertNotNull(response);
        assertEquals(expectedResponse.id(), response.id());
        assertEquals(expectedResponse.name(), response.name());
        assertEquals(expectedResponse.species(), response.species());
        assertEquals(expectedResponse.height(), response.height());
        assertEquals(expectedResponse.weight(), response.weight());

        verify(petRepository).findById(petId);
        verify(petMapper).updateEntityFromDTO(updatedDTO, existingPet);
        verify(petRepository).save(existingPet);
        verify(petMapper).toPetResponseDTO(updatedPet);
    }

    @Test
    void updatePetNullId() {
        PetUpdateRequestDTO updatedPetDto = new PetUpdateRequestDTO();
        updatedPetDto.setName("Apolo");
        updatedPetDto.setSpecies(Species.DOG);
        updatedPetDto.setHeight(30);
        updatedPetDto.setWeight(BigDecimal.valueOf(10.0));

        assertThrows(IllegalArgumentException.class,
                () -> petService.updatePet(null,updatedPetDto),
                "ID não pode ser nulo");
    }

    @Test
    void updatePetInvalidId() {
        PetUpdateRequestDTO updatedPetDto = new PetUpdateRequestDTO();
        updatedPetDto.setName("Apolo");
        updatedPetDto.setSpecies(Species.DOG);
        updatedPetDto.setHeight(30);
        updatedPetDto.setWeight(BigDecimal.valueOf(10.0));

        assertThrows(IllegalArgumentException.class,
                () -> petService.updatePet(-1L,updatedPetDto),
                "ID não pode ser nulo");
    }

    // c

    @Test
    void getPetWithValidId() {
        Long validId = 1L;
        Pet pet = new Pet();
        pet.setId(validId);
        pet.setName("Apolo");
        pet.setSpecies(Species.DOG);
        pet.setHeight(30);
        pet.setWeight(BigDecimal.valueOf(10.0));

        PetResponseDTO petResponseDTO = new PetResponseDTO(
                pet.getId(),
                pet.getCreatedAt(),
                pet.getUpdatedAt(),
                pet.getName(),
                pet.getGender(),
                pet.getAge(),
                pet.getWeight(),
                pet.getBreed(),
                pet.getSpecies(),
                pet.getHeight(),
                pet.getPhotoUrl()
        );

        when(petRepository.findById(validId)).thenReturn(Optional.of(pet));
        when(petMapper.toPetResponseDTO(pet)).thenReturn(petResponseDTO);

        PetResponseDTO response = petService.getPet(validId);

        assertNotNull(response);
        assertEquals(validId, response.id());
        assertEquals("Apolo", response.name());
        assertEquals(Species.DOG, response.species());
        assertEquals(30, response.height());
        assertEquals(BigDecimal.valueOf(10.0), response.weight());
    }

    @Test
    void getPetByIdNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> petService.getPet(null),
                "ID não pode ser nulo");
    }

    @Test
    void getPetByIdFalseInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> petService.getPet(-1L),
                "ID não pode ser negativo");
    }

    // d

    @Test
    void deletePetWithValidId() {
        Long validId = 1L;

        when(petRepository.existsById(validId)).thenReturn(true);

        assertDoesNotThrow(() -> petService.deletePet(validId));

        verify(petRepository).deleteById(validId);
    }

    @Test
    void deletePetWithIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> petService.deletePet(null),
                "ID não pode ser nulo");
    }

    @Test
    void deletePetWithInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> petService.deletePet(-1L),
                "ID não pode ser negativo");
    }

    // Structure Tests

    @Test
    void updateNotFoundPet() {
        PetUpdateRequestDTO updatedPetDto = new PetUpdateRequestDTO();
        updatedPetDto.setName("Apolo");
        updatedPetDto.setSpecies(Species.DOG);
        updatedPetDto.setHeight(30);
        updatedPetDto.setWeight(BigDecimal.valueOf(10.0));

        when(petRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> petService.updatePet(30L, updatedPetDto),
                "Pet não encontrado");
    }

    @Test
    void deleteNotFoundPet() {

        when(petRepository.existsById(30L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> petService.deletePet(30L),
                "Pet não encontrado");
    }

    @Test
    void getNotFoundPet() {
        when(petRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> petService.getPet(30L),
                "Pet não encontrado");
    }
}