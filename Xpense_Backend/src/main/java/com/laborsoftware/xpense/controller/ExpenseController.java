package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ExpenseMapper;
import com.laborsoftware.xpense.service.ExpenseService;
import com.laborsoftware.xpense.service.ProjectService;
import com.laborsoftware.xpense.service.UserService;
import com.laborsoftware.xpense.service.WeeklyTimecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ExpenseController {

    public final ExpenseService expenseService;

    public final UserService userService;

    public final ProjectService projectService;

    public final WeeklyTimecardService weeklyTimecardService;

    @Autowired
    public final ExpenseMapper expenseMapper;

    public ExpenseController(
            ExpenseService expenseService,
            ExpenseMapper expenseMapper,
            UserService userService,
            ProjectService projectService,
            WeeklyTimecardService weeklyTimecardService
    ) {
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.userService = userService;
        this.projectService = projectService;
        this.weeklyTimecardService = weeklyTimecardService;
    }

    @PostMapping("/expenses")
    ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        try {
            Expense expense = expenseMapper.toEntity(expenseDTO);

            Optional<ApplicationUser> user = userService.findOne(expenseDTO.getUserId());
            Optional<Project> project = projectService.findOne(expenseDTO.getProjectId());
            Optional<WeeklyTimecard> weeklyTimecard = weeklyTimecardService.findOne(expenseDTO.getWeeklyTimecardId());

            if(user.isPresent()) {
                expense.setUser(user.get());
            }
            if(project.isPresent()) {
                expense.setProject(project.get());
            }
            if(weeklyTimecard.isPresent()) {
                expense.setWeeklyTimecard(weeklyTimecard.get());
            }
            expense = expenseService.save(expense);

            ExpenseDTO result = expenseMapper.toDto(expense);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/expenses/{id}")
    ResponseEntity<ExpenseDTO> updateExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long id) {
        try {
            Optional<Expense> optionalExpense = expenseService.findOne(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();
                expense = expenseMapper.toEntity(expenseDTO);
                expense = expenseService.save(expense);
                ExpenseDTO result = expenseMapper.toDto(expense);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/expenses")
    ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<Expense> expenses = expenseService.findAll();
        List<ExpenseDTO> result = expenses.stream().map(expenseMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/expenses/{id}")
    ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long id) {
        try {
            Optional<Expense> optionalExpense = expenseService.findOne(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();
                ExpenseDTO result = expenseMapper.toDto(expense);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/expenses/{id}")
    ResponseEntity<ExpenseDTO> deleteExpense(@PathVariable Long id) {
        try {
            Expense expenseToDelete = expenseService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            expenseService.delete(id);
            ExpenseDTO result = expenseMapper.toDto(expenseToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
