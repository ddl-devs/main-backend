package br.com.ifrn.ddldevs.pets_backend.mapper;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username")
    @Mapping(target = "keycloakId")
    @Mapping(target = "email")
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "dateOfBirth")
    @Mapping(target = "photoUrl")
    User toEntity(UserRequestDTO dto);

    List<UserResponseDTO> toDTOList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username")
    @Mapping(target = "keycloakId")
    @Mapping(target = "email")
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "dateOfBirth")
    @Mapping(target = "photoUrl")
    void updateEntityFromDTO(UserRequestDTO dto, @MappingTarget User user);
}
