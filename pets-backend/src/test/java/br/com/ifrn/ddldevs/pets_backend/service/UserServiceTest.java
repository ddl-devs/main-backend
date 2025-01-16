package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.user.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.user.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.mapper.UserMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    private Validator validator;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(keycloak.realm("pets-backend")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // a

    @Test
    void shouldPassValidationForValidUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "john", "1abc23", "john@email.com",
                "John", "Doe",
                LocalDate.of(1990, 1, 15),
                "aws.12bs.bucket.com", "user!123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(userRequestDTO);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void shouldFailValidationWhenFieldsAreNull() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null, "Doe",
                null, null, null,
                LocalDate.of(1990, 1, 15),
                "aws.12bs.bucket.com", null
        );

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(userRequestDTO);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(5, violations.size());
    }

    @Test
    void shouldFailValidationEmailInvalid() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "john", "1abc23", "john123email.com",
                "John", "Doe",
                LocalDate.of(1990, 1, 15),
                "aws.12bs.bucket.com", "user!123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(userRequestDTO);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size(), "Expected exactly one validation error");
    }

    @Test
    void shouldFailWhenUsernameAndEmailExists() {
        User existingUser = new User(1L, "1abc23", "john",
                "John", "Doe", "john@email.com",
                LocalDate.of(1990, 1, 15),
                "www.foto.url", new ArrayList<>());

        User duplicateUser = new User(1L, "1abc23", "john",
                "John", "Doe", "john@email.com",
                LocalDate.of(1990, 1, 15),
                "www.foto.url", new ArrayList<>());

        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userRepository.save(existingUser);

        when(userRepository.save(duplicateUser)).thenThrow(
                DataIntegrityViolationException.class);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.save(duplicateUser),
                "Expected save to throw DataIntegrityViolationException, but it didn't"
        );

        verify(userRepository, times(1)).save(existingUser);
        verify(userRepository, times(1)).save(duplicateUser);
    }

    @Test
    void shouldFailValidationWhenMinAgeIsFalse() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "john", "1abc23", "john@email.com",
                "John", "Doe",
                LocalDate.of(2015, 1, 15),
                "aws.12bs.bucket.com", "user!123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(userRequestDTO);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Usuário tem que ter pelo menos 13 anos", violations.iterator().next().getMessage());
    }

    // b

    @Test
    void getAllUsersAndReturnAListOfSizeOne() {
        User user = new User(1L, "1abc23", "john", "John", "Doe", "john" +
                "@email" +
                ".com", LocalDate.of(1990, 1, 15), "www.foto.url",
                new ArrayList<>());

        when(userRepository.findAll()).thenReturn(List.of(user));

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(),
                user.getUsername(),
                user.getKeycloakId(),
                user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getPhotoUrl());
        when(userMapper.toDTOList(List.of(user))).thenReturn(List.of(userResponseDTO));

        List<UserResponseDTO> users = userService.listUsers();

        assertEquals(1, users.size());
    }

    // c

    @Test
    void getUserByIdTrue() {
        User user = new User(1L, "1abc23", "john", "John", "Doe", "john" +
                "@email" +
                ".com", LocalDate.of(1990, 1, 15), "www.foto.url",
                new ArrayList<>());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(),
                user.getUsername(),
                user.getKeycloakId(),
                user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getPhotoUrl());
        when(userMapper.toResponseDTO(user)).thenReturn(userResponseDTO);

        UserResponseDTO userById = userService.getUserById(1L);

        assertEquals(user.getId(), userById.id());
        assertNotNull(userById);
    }

    @Test
    void getUserByIdFalse() {
        when(userRepository.findById(3498L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(3498L));
    }

    // d

    @Test
    void deleteUserTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserNotFound() {
        when(userRepository.existsById(3498L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(3498L));
    }
}
