package br.com.ifrn.ddldevs.pets_backend.mapper;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PetMapper {

    PetResponseDTO toPetResponseDTO(Pet pet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Pet toEntity(PetRequestDTO petRequestDTO);

    List<PetResponseDTO> toDTOList(List<Pet> pets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDTO(PetRequestDTO petRequestDTO, @MappingTarget Pet pet);
}
