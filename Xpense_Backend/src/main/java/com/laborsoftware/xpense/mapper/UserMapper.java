package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserTimecardMapper.class})
public interface UserMapper {
    User toEntity(UserDTO userDTO);
}
