package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTimecardMapper {
    UserTimecard toEntity(UserTimecardDTO userTimecardDTO);
}
