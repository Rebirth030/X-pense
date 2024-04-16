package com.example.xpense_app.home.model

import com.example.xpense_app.home.service.Expense
import com.example.xpense_app.home.service.ExpenseService

class ExpenseViewModel {
    private val expenseService: ExpenseService = ExpenseService()

    fun saveExpense(
        expense: Expense,
        onSuccess: (Expense) -> Unit,
        onError: (Exception) -> Unit
    ) {
        expenseService.saveExpense(expense,
            onSuccess = {
                onSuccess(it)
            },
            onError = {
                onError(it)
            }
        )
    }

    fun updateExpense(
        expense: Expense,
        onSuccess: (Expense) -> Unit,
        onError: (Exception) -> Unit
    ) {

    }

    fun getExpenses(
        id: Long,
        onSuccess: (List<Expense>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        expenseService.getExpenses(id,
            onSuccess = {
                onSuccess(it)
            },
            onError = {})
    }
}