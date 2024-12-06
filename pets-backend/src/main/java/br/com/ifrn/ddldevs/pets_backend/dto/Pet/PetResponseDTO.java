package br.com.ifrn.ddldevs.pets_backend.dto.Pet;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;

import java.util.Date;

public record PetResponseDTO(
        Long id,
        String name,
        Gender gender,
        Integer age,
        Double weight,
        String breed,
        Integer height,
        Date dateOfBirth,
        String photoUrl
) {
}
