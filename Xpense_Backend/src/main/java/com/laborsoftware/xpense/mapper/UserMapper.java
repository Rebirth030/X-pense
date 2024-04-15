package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserTimecardMapper.class})
public interface UserMapper {
    @Mapping(source = "userTimecardId", target = "userTimecard.id")
    ApplicationUser toEntity(UserDTO userDTO);

    @Mapping(source = "userTimecard.id", target = "userTimecardId")
    UserDTO toDto(ApplicationUser applicationUser);
}
