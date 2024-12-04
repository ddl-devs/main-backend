package br.com.ifrn.ddldevs.pets_backend.auth;

import br.com.ifrn.ddldevs.pets_backend.dto.LoginRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.KeycloakException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping("/")
    public ResponseEntity<String> generateKeycloakToken(@RequestBody LoginRequestDTO body) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", body.clientId());
        formData.add("username", body.username());
        formData.add("password", body.password());
        formData.add("grant_type", body.grantType());

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(formData, headers);

        try {
            var response = rt.postForEntity(
                    "http://localhost:8082/realms/pets-backend/protocol/openid-connect/token",
                    entity,
                    String.class
            );

            return response;
        } catch (HttpClientErrorException e) {
            throw new KeycloakException("Dados não encontrados no Keycloak - " + e.getStatusCode());
        }
    }
}
