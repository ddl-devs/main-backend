package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;


@DataJpaTest
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
        dto.setSpecies("Dog");
        dto.setHeight(30);
        dto.setWeight(10.0);

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setHeight(dto.getHeight());
        pet.setWeight(dto.getWeight());
        pet.setUser(user);

        PetResponseDTO petResponse = new PetResponseDTO(
                pet.getId(),
                pet.getName(),
                pet.getGender(),
                pet.getAge(),
                pet.getWeight(),
                pet.getBreed(),
                pet.getSpecies(),
                pet.getHeight(),
                pet.getDateOfBirth(),
                pet.getPhotoUrl()
        );

        when(petMapper.toEntity(dto)).thenReturn(pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.toPetResponseDTO(pet)).thenReturn(petResponse);

        PetResponseDTO response = petService.createPet(dto);

        assertNotNull(response);
        assertEquals("Apolo", response.name());
        assertEquals("Dog", response.species());
        assertEquals(30, response.height());
        assertEquals(10.0, response.weight(), 0.01);

        verify(userRepository).findById(1L);
        verify(petMapper).toEntity(dto);
        verify(petRepository).save(any(Pet.class));
        verify(petMapper).toPetResponseDTO(pet);
    }

    @Test
    void createPetInvalidInformation() {
        PetRequestDTO dto = new PetRequestDTO();
        dto.setName("");
        dto.setWeight(-5.0);
        dto.setSpecies("");
        dto.setBreed("");
        dto.setHeight(0);
        dto.setDateOfBirth(LocalDate.of(2026, 1, 1));
        dto.setUserId(null);

        Set<ConstraintViolation<PetRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(8, violations.size());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name") && v.getMessage().contains("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weight") && v.getMessage().contains("must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("species") && v.getMessage().contains("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("breed") && v.getMessage().contains("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("height") && v.getMessage().contains("must be greater than 0")));
    }

}