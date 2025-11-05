package org.example.supplychainx.Mappers;

import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto(User user);
    User toEntity(UserResponseDTO userResponseDTO);
}
