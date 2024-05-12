package com.example.xpense_app.controller.services

import Expense
import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.interfaces.ExpenseAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpenseService {
    companion object {
        private val apiService =
            RetrofitInstance.getAPIService(ExpenseAPIService::class) as ExpenseAPIService

        /**
         * Get all expenses from the server.
         *
         * @param token the token of the user
         * @param onSuccess the callback when the expenses are successfully fetched
         * @param onError the callback when an error occurs
         */
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

        /**
         * Create an expense on the server.
         *
         * @param expense the expense to create
         * @param token the token of the user
         * @param onSuccess the callback when the expense is successfully created
         * @param onError the callback when an error occurs
         */
        fun createExpense(
            expense: Expense,
            token: String,
            onSuccess: suspend (Expense) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.createExpense(token, expense)
                    onSuccess(response.body()!!)
                } catch (e: Exception) {
                    onError(e)
                }
            }

        }

        fun updateExpense(
            expense: Expense,
            token: String,
            onSuccess: suspend (Expense) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.updateExpense(token, expense.id!!, expense)
                    onSuccess(response.body()!!)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
}
