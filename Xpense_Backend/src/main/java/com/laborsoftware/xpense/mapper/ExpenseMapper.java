package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class})
public interface ExpenseMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "projectId", target = "project.id")
    Expense toEntity(ExpenseDTO expenseDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "project.id", target = "projectId")
    ExpenseDTO toDto(Expense expense);
}
