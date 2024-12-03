package br.com.ifrn.ddldevs.pets_backend.dto;

import java.util.Date;

public record UserResponseDTO(Long id, String username, String email, String firstName, String lastName, Date dateOfBirth, String photoUrl) {
}
