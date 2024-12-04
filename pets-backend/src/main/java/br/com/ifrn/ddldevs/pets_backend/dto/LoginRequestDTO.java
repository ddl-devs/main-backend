package br.com.ifrn.ddldevs.pets_backend.dto;

public record LoginRequestDTO(String username, String password, String clientId, String grantType) {
}
