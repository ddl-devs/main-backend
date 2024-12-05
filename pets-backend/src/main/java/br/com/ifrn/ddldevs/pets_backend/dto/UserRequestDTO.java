package br.com.ifrn.ddldevs.pets_backend.dto;

import java.util.Date;

// keycloakId is temporary
public record UserRequestDTO(String username, String keycloakId, String email, String firstName, String lastName,
                             Date dateOfBirth, String photoUrl, String password) {
}
