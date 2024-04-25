package com.example.xpense_app.controller.services

import Expense
import com.example.xpense_app.controller.service.ExpenseAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpenseService {
    private var _expenses = mutableListOf<Expense>()

    fun saveExpense(
        expense: Expense,
        onSuccess: (Expense) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = ExpenseAPIService.getInstance()
            try {
                val response = if (expense.id == null) {
                    apiService.createExpense(expense)
                } else {
                    apiService.updateExpense(expense.id, expense)
                }
                val expense = Expense(
                    response.id,
                    response.startDateTime,
                    response.endDateTime,
                    response.state,
                    response.projectId,
                    response.userId,
                    response.weeklyTimecardId
                )
                onSuccess(expense)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getExpenses(
        id: Long,
        onSuccess: (List<Expense>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = ExpenseAPIService.getInstance()
            try {
                val response = apiService.getExpenses()
                System.out.println(response)
                _expenses.clear()
                _expenses.addAll(response.body()!!)
                onSuccess(response.body()!!)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getExpensesOfProject(
        id: Long,
        onSuccess: (List<Expense>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = ExpenseAPIService.getInstance()
            try {
                val response = apiService.getExpensesOfProject(id)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
