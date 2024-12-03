package br.com.ifrn.ddldevs.pets_backend.keycloak;

import br.com.ifrn.ddldevs.pets_backend.dto.KcUserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;

@Service
public class KeycloakService {

    @Value("${keycloak.realm}") String realmName;

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public KcUserResponseDTO createUser(UserRequestDTO dto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.password());
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        try (Response response = keycloak.realm(realmName)
                .users()
                    .create(user)) {

            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                String responseBody = response.readEntity(String.class);
                throw new RuntimeException("Error creating user: " + response.getStatus() + " - " + responseBody);
            }

            URI location = response.getLocation();
            String userId = location.getPath().replaceAll(".*/([^/]+)$", "$1");

            UserRepresentation createdUser = keycloak.realm(realmName)
                    .users()
                    .get(userId)
                    .toRepresentation();

            return new KcUserResponseDTO(
                    createdUser.getId(),
                    createdUser.getUsername(),
                    createdUser.getEmail(),
                    createdUser.getFirstName(),
                    createdUser.getLastName()
            );
        }
    }
}
