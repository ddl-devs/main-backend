package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.KcUserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.keycloak.KeycloakService;
import br.com.ifrn.ddldevs.pets_backend.mapper.UserMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
//        KcUserResponseDTO keycloakUser = keycloakService.createUser(dto);
//        user.setKeycloakId(keycloakUser.id());

        User user = userMapper.toEntity(dto);

        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> listUsers() {
        var users = userRepository.findAll();

        return userMapper.toDTOList(users);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        userMapper.updateEntityFromDTO(dto, user);

        var updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
}
