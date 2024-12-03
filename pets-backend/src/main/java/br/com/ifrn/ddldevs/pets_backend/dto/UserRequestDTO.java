package br.com.ifrn.ddldevs.pets_backend.dto;

import java.util.Date;

public record UserRequestDTO(String username, String email, String firstName, String lastName, Date dateOfBirth, String photoUrl, String password) {
}
