package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.domain.dto.ExpenseDTO;
import com.laborsoftware.xpense.domain.enumeration.ExpenseState;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ExpenseMapper;
import com.laborsoftware.xpense.repository.ExpenseRepository;
import com.laborsoftware.xpense.repository.UserRepository;
import com.laborsoftware.xpense.service.crud.ICrudService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping
@Service
@Transactional
public class ExpenseService implements ICrudService<ExpenseDTO, Long> {
    private final ExpenseRepository expenseRepository;

    @Autowired
    public final ExpenseMapper expenseMapper;

    public final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseMapper expenseMapper,
            UserRepository userRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.userRepository = userRepository;
    }

    @Override
    @PostMapping("/expenses")
    public ResponseEntity<ExpenseDTO> save(@RequestHeader("Authorization") String token, @RequestBody ExpenseDTO expenseDTO) {
        logger.debug("Request to save Expense {} ", expenseDTO);
        try {
            Expense expense = expenseMapper.toEntity(expenseDTO);
            expense = expenseRepository.save(expense);
            ExpenseDTO result = expenseMapper.toDto(expense);

            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @Override
    @PutMapping("/expenses/{id}")
    public ResponseEntity<ExpenseDTO> update(@RequestHeader("Authorization") String token, @RequestBody ExpenseDTO expenseDTO, @PathVariable Long id) {
        try {
            Optional<Expense> optionalExpense = expenseRepository.findById(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();
                expense = expenseMapper.toEntity(expenseDTO);
                expense = expenseRepository.save(expense);
                ExpenseDTO result = expenseMapper.toDto(expense);

                double hoursOfToday = this.getHoursOfToday(expense.getUser());
                if (hoursOfToday >= 10 && expense.getUser().getSuperior() != null) {
                    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                    MailService mailService = new MailService(mailSender);

                    mailService.createExpenseOverTenHourEmail(expense);
                }

                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private double getHoursOfToday(ApplicationUser user) {
        List<Expense> allExpensesToday = expenseRepository
                .findAllByStartDateTimeIsTodayAndUserId(ZonedDateTime.now(), user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return allExpensesToday.stream()
                .mapToDouble(exp -> {
                    ZonedDateTime startLocalDateTime = exp.getStartDateTime();
                    ZonedDateTime endLocalDateTime = (exp.getEndDateTime() != null) ?
                            exp.getEndDateTime() : ZonedDateTime.parse(ZonedDateTime.now().format(formatter));
                    long durationInMinutes = Duration.between(startLocalDateTime, endLocalDateTime).toMinutes();
                    return durationInMinutes / 60.0;
                })
                .sum();
    }

    @Override
    @DeleteMapping("/expenses/{id}")
    public void delete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        logger.debug("Request to delete Expense {} ", id);
        try {
            Optional<Expense> expenseToDelete = expenseRepository.findById(id);
            if (expenseToDelete.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
            expenseRepository.deleteById(id);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseDTO>> findAll(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.split(" ")[1];
            List<Expense> expenses = expenseRepository.findAllByApplicationUser(userRepository.findByToken(tokenValue)
                    .orElseThrow());
            List<ExpenseDTO> result = expenses.stream().map(expenseMapper::toDto).toList();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @GetMapping("/expenses/{id}")
    public ResponseEntity<ExpenseDTO> findOne(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        logger.debug("Request to get all Expense");
        try {
            Optional<Expense> optionalExpense = expenseRepository.findById(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();
                ExpenseDTO result = expenseMapper.toDto(expense);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/expenses/project/{id}")
    public ResponseEntity<List<ExpenseDTO>> findAllExpensesOfProject(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            List<Expense> allProjectExpenses = expenseRepository.findAllByProjectId(id);
            List<ExpenseDTO> result = allProjectExpenses.stream().map(expenseMapper::toDto).toList();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
