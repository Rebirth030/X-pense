package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Expense;
import com.laborsoftware.xpense.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService implements ICrudService<Expense, Long> {

    Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        logger.debug("Request to save Expense {} ", expense);
        Expense result = null;
        try {
            result = expenseRepository.save(expense);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete Expense {} ", id);
        try {
            expenseRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<Expense> findAll() {
        logger.debug("Request to get all Expense");
        return expenseRepository.findAll();
    }

    @Override
    public Optional<Expense> findOne(Long id) {
        logger.debug("Request to get all Expense");
        return expenseRepository.findById(id);
    }
}
