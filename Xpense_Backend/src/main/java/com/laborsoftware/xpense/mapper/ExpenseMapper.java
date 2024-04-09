package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class, WeeklyTimecardMapper.class})
public interface ExpenseMapper {
    Expense toEntity(ExpenseDTO expenseDTO);
}
