package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class, WeeklyTimecardMapper.class})
public interface ExpenseMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "weeklyTimecardId", target = "weeklyTimecard.id")
    Expense toEntity(ExpenseDTO expenseDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "weeklyTimecard.id", target = "weeklyTimecardId")
    ExpenseDTO toDto(Expense expense);
}
