package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ExpenseMapper;
import com.laborsoftware.xpense.service.ExpenseService;
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

   @Autowired
   public final ExpenseMapper expenseMapper;

   public ExpenseController(
           ExpenseService expenseService,
           ExpenseMapper expenseMapper
   ) {
       this.expenseService = expenseService;
       this.expenseMapper = expenseMapper;
   }

    @PostMapping("/expenses")
    ResponseEntity<Expense> createEvent(@RequestBody ExpenseDTO expenseDTO) {
        try {
            Expense expense = expenseMapper.toEntity(expenseDTO);
            return ResponseEntity.ok().body(expenseService.save(expense));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/expenses/{id}")
    ResponseEntity<Expense> updateEvent(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long id) {
        try {
            Optional<Expense> optionalExpense = expenseService.findOne(id);
            if(optionalExpense.isPresent()) {
                return ResponseEntity.ok().body(expenseService.save(expenseMapper.toEntity(expenseDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/expenses")
    ResponseEntity<List<Expense>> getAllEvents() {
        return new ResponseEntity<>(expenseService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/expenses/{id}")
    ResponseEntity<Expense> getEvent(@PathVariable Long id) {
        try {
            Optional<Expense> optionalExpense = expenseService.findOne(id);
            if(optionalExpense.isPresent()) {
                optionalExpense.get();
                return ResponseEntity.ok().body(optionalExpense.get());
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
    ResponseEntity<Expense> deleteEvent(@PathVariable Long id) {
        try {
            Expense expenseToDelete = expenseService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            expenseService.delete(id);
            return new ResponseEntity<>(expenseToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
