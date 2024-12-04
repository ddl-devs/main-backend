package br.com.ifrn.ddldevs.pets_backend.keycloak;

import br.com.ifrn.ddldevs.pets_backend.dto.KcUserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.LoginRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.LogoutRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.KeycloakException;

import jakarta.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

@Service
public class KeycloakService {

    @Value("${keycloak.realm}")
    String realmName;

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String generateToken(LoginRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", dto.clientId());
        formData.add("username", dto.username());
        formData.add("password", dto.password());
        formData.add("grant_type", dto.grantType());

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(formData, headers);

        try {
            var response = rt.exchange(
                    "http://localhost:8082/realms/" + realmName + "/protocol/openid-connect/token",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new KeycloakException("Dados não encontrados no Keycloak - " + e.getStatusCode());
        }
    }

    public String logout(LogoutRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", dto.client_id());
        formData.add("refresh_token", dto.refresh_token());

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(formData, headers);

        try {
            var response = rt.postForEntity(
                    "http://localhost:8082/realms/" + realmName + "/protocol/openid-connect/logout",
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new KeycloakException("Erro ao fazer logout no Keycloak" + e.getStatusCode());
        }
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
