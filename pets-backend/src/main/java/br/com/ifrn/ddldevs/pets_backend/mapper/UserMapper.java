package br.com.ifrn.ddldevs.pets_backend.mapper;

import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.user.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.user.UserResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);

    List<UserResponseDTO> toDTOList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UserRequestDTO dto, @MappingTarget User user);
}
