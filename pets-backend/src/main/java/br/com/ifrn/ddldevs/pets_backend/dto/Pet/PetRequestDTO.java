package br.com.ifrn.ddldevs.pets_backend.dto.Pet;

import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Schema(description = "DTO para requisições de Pets")
public class PetRequestDTO {

    //@Schema(description = "Nome do Pet", example = "Apolo")
    private String name;

    //@Schema(description = "Sexo do Pet", example = "MALE")
    private Gender gender;

    //@Schema(description = "Idade do Pet", example = "2")
    private Integer age;

    //Schema(description = "Peso do Pet (Kg)", example = "2.5")
    private Double weight;

    //@Schema(description = "Raça do Pet", example = "Yorkshire")
    private String breed;

    //@Schema(description = "Altura do Pet (Cm)", example = "30")
    private Integer height;

    //@Schema(description = "Data de Nascimento do Pet", example = "2024-12-05T14:30:00Z")
    private Date dateOfBirth;

    //@Schema(description = "Foto do Pet", example = "www.foto.com")
    private String photoUrl;

    //@Schema(description = "ID do usuário dono", example = "1")
    @NotBlank(message = "ID do dono é obrigatório")
    private Long user_id;
}
