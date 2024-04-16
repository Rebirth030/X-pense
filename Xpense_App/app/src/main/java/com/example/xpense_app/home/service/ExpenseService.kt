package com.example.xpense_app.home.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class Expense(
    @SerializedName("id") val id: Int?,
    @SerializedName("startDateTime") val startDateTime: LocalDateTime,
    @SerializedName("endDateTime") val endDateTime: LocalDateTime?,
    @SerializedName("state") val state: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("projectId") val projectId: Long,
    @SerializedName("weeklyTimecardId") val weeklyTimecardId: Long
)

class ExpenseService : ViewModel() {
    private var _expenses = mutableListOf<Expense>()
    var errorMessage: String by mutableStateOf("")

    fun saveExpense(
        expense: Expense,
        onSuccess: (Expense) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
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
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                val response = apiService.getExpenses()
                onSuccess(response.body()!!)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}