package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ApplicationUser  toEntity(UserDTO userDTO);

    UserDTO toDto(ApplicationUser applicationUser);


}
