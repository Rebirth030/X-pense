package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-09T13:36:13+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ExpenseMapperImpl implements ExpenseMapper {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeeklyTimecardMapper weeklyTimecardMapper;

    @Override
    public Expense toEntity(ExpenseDTO expenseDTO) {
        if ( expenseDTO == null ) {
            return null;
        }

        Expense expense = new Expense();

        expense.setId( expenseDTO.getId() );
        expense.setStartDateTime( expenseDTO.getStartDateTime() );
        expense.setEndDateTime( expenseDTO.getEndDateTime() );
        expense.setState( expenseDTO.getState() );
        expense.setUser( userMapper.toEntity( expenseDTO.getUser() ) );
        expense.setProject( projectMapper.toEntity( expenseDTO.getProject() ) );
        expense.setWeeklyTimecard( weeklyTimecardMapper.toEntity( expenseDTO.getWeeklyTimecard() ) );

        return expense;
    }
}
