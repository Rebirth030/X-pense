package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import com.laborsoftware.xpense.domain.enumeration.ExpenseState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExpenseRepositoryTest {


    @Autowired
    private  ExpenseRepository expenseRepository;
    @Autowired
    private  ProjectRepository projectRepository;
    @Autowired
    private  UserRepository UserRepository;

    /**
     * Testet die Methode findAllByProjectId
     */
    @Test
    public void testFindAllByProjectId() {
        Project project = new Project();
        project = projectRepository.save(project);
        ArrayList<Expense> expenses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Expense expense = new Expense();
            expense.setStartDateTime(ZonedDateTime.now());
            expense.setEndDateTime(ZonedDateTime.now());
            expense.setState(ExpenseState.PAUSED);
            expense.setProject(project);
            expense.setDescription("Description: " + i);
            expenses.add(expenseRepository.save(expense));
        }

        assertEquals(10, expenseRepository.findAllByProjectId(project.getId()).size());
        assertArrayEquals(expenses.toArray(), expenseRepository.findAllByProjectId(project.getId()).toArray());
    }

    /**
     * Testet die Methode findAllByApplicationUser
     */
    @Test
    public void testFindAllByApplicationUser() {

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser = UserRepository.save(applicationUser);
        ArrayList<Expense> expenses = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Expense expense = new Expense();
            expense.setStartDateTime(ZonedDateTime.now());
            expense.setEndDateTime(ZonedDateTime.now());
            expense.setState(ExpenseState.PAUSED);
            expense.setUser(applicationUser);
            expense.setDescription("Description: " + i);
            expenses.add(expenseRepository.save(expense));
        }

        assertEquals(12, expenseRepository.findAllByApplicationUser(applicationUser).size());
        assertArrayEquals(expenses.toArray(), expenseRepository.findAllByApplicationUser(applicationUser).toArray());
    }

    //Ab hier unnötig aber nur zu Sicherheit

    @Test
    public void testSave() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");
        expense = expenseRepository.save(expense);
        assertNotNull(expense.getId());
    }

    @Test
    public void testUpdate() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");
        expense = expenseRepository.save(expense);
        expense.setDescription("Updated Description");
        expense = expenseRepository.save(expense);
        assertEquals("Updated Description", expense.getDescription());
    }

    @Test
    public void testDelete() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");
        expense = expenseRepository.save(expense);
        expenseRepository.deleteById(expense.getId());
        assertFalse(expenseRepository.findById(expense.getId()).isPresent());
    }

    @Test
    public void testFindById() {
        Expense expense = new Expense();
        expense.setStartDateTime(ZonedDateTime.now());
        expense.setEndDateTime(ZonedDateTime.now());
        expense.setState(ExpenseState.PAUSED);
        expense.setDescription("Description: ");
        expense = expenseRepository.save(expense);
        assertNotNull(expenseRepository.findById(expense.getId()));
    }
}
