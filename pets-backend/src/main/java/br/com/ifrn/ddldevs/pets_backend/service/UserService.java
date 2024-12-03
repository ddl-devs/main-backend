package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.KcUserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.keycloak.KeycloakService;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository repository;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        KcUserResponseDTO keycloakUser = keycloakService.createUser(dto);

        User user = new User();
        user.setKeycloakId(keycloakUser.id());
        user.setUsername(dto.username());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setDateOfBirth(dto.dateOfBirth());
        user.setPhotoUrl(dto.photoUrl());
        repository.save(user);

        return new UserResponseDTO(
                user.getId(),
                keycloakUser.username(),
                keycloakUser.email(),
                keycloakUser.firstName(),
                keycloakUser.lastName(),
                user.getDateOfBirth(),
                user.getPhotoUrl()
        );
    }
}
