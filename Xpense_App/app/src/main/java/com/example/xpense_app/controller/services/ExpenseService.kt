package com.example.xpense_app.controller.services

import Expense
import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.service.ExpenseAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpenseService {
    companion object {
        private val apiService =
            RetrofitInstance.getAPIService(ExpenseAPIService::class) as ExpenseAPIService

        fun getExpenses(
            token: String,
            onSuccess: suspend (List<Expense>) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.getExpenses(token)
                    onSuccess(response.body().orEmpty())
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
        fun createExpense(
            expense: Expense,
            token: String,
            onSuccess: suspend (Expense) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.createExpense(token, expense)
                    onSuccess(response)
                } catch (e: Exception) {
                    onError(e)
                }
            }

        }
    }

/*
    fun saveExpense(
        expense: Expense,
        onSuccess: (Expense) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val response = if (expense.id == null) {
                    apiService.createExpense(expense)
                } else {
                    apiService.updateExpense(expense.id, expense)
                }
                val returnExpense = Expense(
                    response.id,
                    response.startDateTime,
                    response.endDateTime,
                    response.state,
                    response.projectId,
                    response.userId,
                    response.weeklyTimecardId
                )
                onSuccess(returnExpense)
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
            try {
                val response = apiService.getExpensesOfProject(id)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

 */
}
