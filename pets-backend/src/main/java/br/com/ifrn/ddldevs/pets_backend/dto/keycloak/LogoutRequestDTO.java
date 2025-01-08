package br.com.ifrn.ddldevs.pets_backend.dto.keycloak;

public record LogoutRequestDTO(String client_id, String refresh_token) {
}
