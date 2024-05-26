package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import com.laborsoftware.xpense.domain.enumeration.ExpenseState;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ExpenseMapper;
import com.laborsoftware.xpense.repository.ExpenseRepository;
import com.laborsoftware.xpense.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testSaveExpenseSuccessfully() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setStartDateTime(expense.getStartDateTime());
        expenseDTO.setEndDateTime(expense.getEndDateTime());
        expenseDTO.setState(expense.getState());
        expenseDTO.setDescription(expense.getDescription());

        when(expenseMapper.toEntity(Mockito.any(ExpenseDTO.class))).thenReturn(expense);
        when(expenseMapper.toDto(Mockito.any(Expense.class))).thenReturn(expenseDTO);

        when(expenseRepository.save(Mockito.any(Expense.class))).thenReturn(expense);

        ExpenseDTO savedExpense = expenseService.save("token", expenseDTO).getBody();

        assertThat(savedExpense).isNotNull();
    }

    @Test
    public void testSaveExpenseThrowsException() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        when(expenseMapper.toEntity(Mockito.any(ExpenseDTO.class))).thenReturn(new Expense());
        when(expenseRepository.save(Mockito.any(Expense.class))).thenThrow(new RuntimeException());

        ResponseEntity<ExpenseDTO> response = expenseService.save("token", expenseDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateExpenseSuccessfully() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");

        ExpenseDTO expenseDTOUpdated = new ExpenseDTO();
        expenseDTOUpdated.setStartDateTime(ZonedDateTime.now());
        expenseDTOUpdated.setEndDateTime(ZonedDateTime.now());
        expenseDTOUpdated.setState(ExpenseState.PAUSED);
        expenseDTOUpdated.setDescription("Description: ");


        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setStartDateTime(expense.getStartDateTime());
        expenseDTO.setEndDateTime(expense.getEndDateTime());
        expenseDTO.setState(expense.getState());
        expenseDTO.setDescription(expense.getDescription());

        when(expenseMapper.toEntity(Mockito.any(ExpenseDTO.class))).thenReturn(expense);
        when(expenseMapper.toDto(Mockito.any(Expense.class))).thenReturn(expenseDTOUpdated);
        when(expenseRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expense));
        when(expenseRepository.save(Mockito.any(Expense.class))).thenReturn(expense);


        ResponseEntity<ExpenseDTO> response = expenseService.update("token", expenseDTO, 1L);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenseDTOUpdated, response.getBody());
    }

    @Test
    public void testUpdateExpenseThrowsException() {

        ExpenseDTO expenseDTO = new ExpenseDTO();
        when(expenseRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());


        ResponseEntity<ExpenseDTO> response = expenseService.update("token", expenseDTO, 1L);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAllExpensesReturnsFive() {
        ApplicationUser applicationUser = new ApplicationUser();
        Expense expense = new Expense();
        expense.setUser(applicationUser);
        ExpenseDTO expenseDTO = new ExpenseDTO();

        when(userRepository.findByToken(Mockito.anyString())).thenReturn(Optional.of(applicationUser));
        when(expenseRepository.findAllByApplicationUser(applicationUser)).thenReturn(List.of(expense, expense, expense, expense, expense));
        when(expenseMapper.toDto(expense)).thenReturn(expenseDTO);


        ResponseEntity<List<ExpenseDTO>> response = expenseService.findAll("Bearer token");


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
    }

    @Test
    public void testFindAllExpensesThrowsExceptionWhenNoUser() {
        when(userRepository.findByToken("testToken")).thenReturn(Optional.empty());

        ResponseEntity<List<ExpenseDTO>> response = expenseService.findAll("Bearer testToken");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAllExpensesReturnsEmptyWhenNoExpenses() {
        ApplicationUser applicationUser = new ApplicationUser();

        when(userRepository.findByToken("testToken")).thenReturn(Optional.of(applicationUser));
        when(expenseRepository.findAllByApplicationUser(applicationUser)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ExpenseDTO>> response = expenseService.findAll("Bearer testToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testFindOneExpenseSuccessfully() {
        Expense expense = new Expense();
        ExpenseDTO expenseDTO = new ExpenseDTO();

        when(expenseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expense));
        when(expenseMapper.toDto(expense)).thenReturn(expenseDTO);

        ResponseEntity<ExpenseDTO> response = expenseService.findOne("token",1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenseDTO, response.getBody());
    }

    @Test
    public void testFindOneExpenseThrowsException() {
        when(expenseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ExpenseDTO> response = expenseService.findOne("token", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAllExpensesOfProjectSuccessfully() {
        Long projectId = 1L;
        Expense expense = new Expense();
        ExpenseDTO expenseDTO = new ExpenseDTO();

        when(expenseRepository.findAllByProjectId(projectId)).thenReturn(List.of(expense));
        when(expenseMapper.toDto(expense)).thenReturn(expenseDTO);

        // Act
        ResponseEntity<List<ExpenseDTO>> response = expenseService.findAllExpensesOfProject("token", projectId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(expenseDTO, response.getBody().get(0));
    }

    @Test
    public void testFindAllExpensesOfProjectThrowsException() {
        // Arrange
        Long projectId = 1L;

        when(expenseRepository.findAllByProjectId(projectId)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<ExpenseDTO>> response = expenseService.findAllExpensesOfProject("token", projectId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
