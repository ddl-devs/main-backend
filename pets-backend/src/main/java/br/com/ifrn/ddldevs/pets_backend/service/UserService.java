package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserUpdateRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.keycloak.KcUserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.keycloak.KeycloakServiceImpl;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetMapper;
import br.com.ifrn.ddldevs.pets_backend.mapper.UserMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private KeycloakServiceImpl keycloakServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PetMapper petMapper;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        KcUserResponseDTO keycloakUser = keycloakServiceImpl.createUser(dto);

        User user = userMapper.toEntity(dto);
        user.setKeycloakId(keycloakUser.id());

        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> listUsers() {
        var users = userRepository.findAll();

        return userMapper.toDTOList(users);
    }

    public UserResponseDTO getCurrentUser(String loggedUserKeycloakId) {
        var user = userRepository.findByKeycloakId(loggedUserKeycloakId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        });
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id, String loggedUserKeycloakId) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        User user = userRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        throwIfLoggedUserIsDifferentFromUserResource(loggedUserKeycloakId, user);

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO dto,
        String loggedUserKeycloakId) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        User user = userRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        throwIfLoggedUserIsDifferentFromUserResource(loggedUserKeycloakId, user);

        keycloakServiceImpl.updateUser(user.getKeycloakId(), dto);

        userMapper.updateEntityFromDTO(dto, user);

        var updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    public void deleteUser(Long id, String loggedUserKeycloakId) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Usuário não encontrado!")
        );

        throwIfLoggedUserIsDifferentFromUserResource(loggedUserKeycloakId, user);

        keycloakServiceImpl.deleteUser(user.getKeycloakId());

        userRepository.deleteById(id);
    }

    public List<PetResponseDTO> getPetsOfCurrentUser(String loggedUserKeycloakId) {
        var user = userRepository.findByKeycloakId(loggedUserKeycloakId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        });

        return petMapper.toDTOList(user.getPets());
    }

    public List<PetResponseDTO> getPets(Long userId, String loggedUserKeycloakId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        throwIfLoggedUserIsDifferentFromUserResource(loggedUserKeycloakId, user);

        return petMapper.toDTOList(user.getPets());
    }

    public void throwIfLoggedUserIsDifferentFromUserResource(
        String loggedPersonKeycloakId,
        User user
    ) {
        if (!user.getKeycloakId().equals(loggedPersonKeycloakId)) {
            throw new ResourceNotFoundException(
                "Usuário não encontrado"
            );
        }
    }
}
