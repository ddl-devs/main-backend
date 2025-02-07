package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Species;
import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PetControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        petRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("jhon");
        user.setFirstName("Jhon");
        user.setLastName("Doe");
        user.setEmail("jhon@gmail.com");
        user.setKeycloakId("345");
        user.setPets(new ArrayList<>());

        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Should create a pet successfully")
    void shouldCreatePetSuccessfully() throws Exception {

        PetRequestDTO dto = new PetRequestDTO();
        dto.setUserId(1L);
        dto.setName("Apolo");
        dto.setSpecies(Species.DOG);
        dto.setHeight(30);
        dto.setWeight(BigDecimal.valueOf(10.0));

        String requestBody = objectMapper.writeValueAsString(dto);
        System.out.println(requestBody);

        mockMvc.perform(
                post("/pets/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());


        Optional<Pet> savedPet = petRepository.findAll().stream().findFirst();
        assertTrue(savedPet.isPresent());
        assertEquals("Apolo", savedPet.get().getName());
    }
}