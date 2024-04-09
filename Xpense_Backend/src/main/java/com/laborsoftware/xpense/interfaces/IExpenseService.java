package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.Expense;

import java.util.List;
import java.util.Optional;

public interface IExpenseService {
    Expense save(Expense expense);

    void delete(Long id);

    List<Expense> findAll();

    Optional<Expense> findOne(Long id);
}
