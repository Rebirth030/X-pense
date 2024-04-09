package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ExpenseMapper.class})
public interface WeeklyTimecardMapper {

    WeeklyTimecard toEntity(WeeklyTimecardDTO weeklyTimecardDTO);
}
