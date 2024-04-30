package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-30T14:04:40+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ExpenseMapperImpl implements ExpenseMapper {

    @Override
    public Expense toEntity(ExpenseDTO expenseDTO) {
        if ( expenseDTO == null ) {
            return null;
        }

        Expense expense = new Expense();

        expense.setUser( expenseDTOToApplicationUser( expenseDTO ) );
        expense.setProject( expenseDTOToProject( expenseDTO ) );
        expense.setWeeklyTimecard( expenseDTOToWeeklyTimecard( expenseDTO ) );
        expense.setId( expenseDTO.getId() );
        expense.setStartDateTime( expenseDTO.getStartDateTime() );
        expense.setEndDateTime( expenseDTO.getEndDateTime() );
        expense.setState( expenseDTO.getState() );
        expense.setDescription( expenseDTO.getDescription() );

        return expense;
    }

    @Override
    public ExpenseDTO toDto(Expense expense) {
        if ( expense == null ) {
            return null;
        }

        ExpenseDTO expenseDTO = new ExpenseDTO();

        expenseDTO.setUserId( expenseUserId( expense ) );
        expenseDTO.setProjectId( expenseProjectId( expense ) );
        expenseDTO.setWeeklyTimecardId( expenseWeeklyTimecardId( expense ) );
        expenseDTO.setId( expense.getId() );
        expenseDTO.setStartDateTime( expense.getStartDateTime() );
        expenseDTO.setEndDateTime( expense.getEndDateTime() );
        expenseDTO.setState( expense.getState() );
        expenseDTO.setDescription( expense.getDescription() );

        return expenseDTO;
    }

    protected ApplicationUser expenseDTOToApplicationUser(ExpenseDTO expenseDTO) {
        if ( expenseDTO == null ) {
            return null;
        }

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setId( expenseDTO.getUserId() );

        return applicationUser;
    }

    protected Project expenseDTOToProject(ExpenseDTO expenseDTO) {
        if ( expenseDTO == null ) {
            return null;
        }

        Project project = new Project();

        project.setId( expenseDTO.getProjectId() );

        return project;
    }

    protected WeeklyTimecard expenseDTOToWeeklyTimecard(ExpenseDTO expenseDTO) {
        if ( expenseDTO == null ) {
            return null;
        }

        WeeklyTimecard weeklyTimecard = new WeeklyTimecard();

        weeklyTimecard.setId( expenseDTO.getWeeklyTimecardId() );

        return weeklyTimecard;
    }

    private Long expenseUserId(Expense expense) {
        if ( expense == null ) {
            return null;
        }
        ApplicationUser user = expense.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long expenseProjectId(Expense expense) {
        if ( expense == null ) {
            return null;
        }
        Project project = expense.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long expenseWeeklyTimecardId(Expense expense) {
        if ( expense == null ) {
            return null;
        }
        WeeklyTimecard weeklyTimecard = expense.getWeeklyTimecard();
        if ( weeklyTimecard == null ) {
            return null;
        }
        Long id = weeklyTimecard.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
