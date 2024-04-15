package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ExpenseMapper.class, UserTimecardMapper.class})
public interface WeeklyTimecardMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userTimecardId", target = "userTimecard.id")
    WeeklyTimecard toEntity(WeeklyTimecardDTO weeklyTimecardDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "userTimecard.id", target = "userTimecardId")
    WeeklyTimecardDTO toDto(WeeklyTimecard weeklyTimecard);
}
