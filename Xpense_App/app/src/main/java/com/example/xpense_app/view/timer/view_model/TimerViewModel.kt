package com.example.xpense_app.view.timer.view_model

import Expense
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class TimerViewModel(private val currentUser: MutableState<User>) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _currentExpense = MutableStateFlow<Expense?>(null)
    val currentExpense: StateFlow<Expense?> = _currentExpense.asStateFlow()

    private val _projectTimersOnRun = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val projectTimersOnRun: StateFlow<Map<Long, Boolean>> = _projectTimersOnRun.asStateFlow()

    private val _projectTimersStartTime = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val projectTimersStartTime: StateFlow<Map<Long, Long>> = _projectTimersStartTime.asStateFlow()

    private val _projectTimers = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val projectTimers: StateFlow<Map<Long, Long>> = _projectTimers.asStateFlow()

    init {
        viewModelScope.launch {
            loadExpenses()
            loadProjects()
        }
    }


    private fun loadProjects() {
        val projectsDeferred = CompletableDeferred<Unit>()

        ProjectService.getProjects(
            currentUser.value.token,
            onSuccess = {
                _projects.value = it
                if (_projects.value.isNotEmpty()) {
                    _currentProject.value = _projects.value[0]
                }
                Log.d("Expenses size in load projects", expenses.value.size.toString())
                it.map { project ->
                    _projectTimersOnRun.value += (project.id!! to false)
                    _projectTimersStartTime.value += (project.id to 0L)
                    _projectTimers.value += (project.id to 0L)
                }
                if(expenses.value.isNotEmpty())  {
                    val openExpenses =
                        expenses.value.filter { expense ->
                            expense.state == "RUNNING" || expense.state == "PAUSED"
                        }
                    openExpenses.forEach { expense ->
                        if(expense.state == "RUNNING") {
                            _currentExpense.value = expense
                            _currentProject.value =
                                projects.value.find { project -> project.id == expense.projectId!! }
                            _projectTimersOnRun.value += (expense.projectId!! to true)
                        } else {
                            _projectTimersOnRun.value += (expense.projectId!! to false)
                        }
                        this.updateProjectTimers(expense)
                    }
                }
                projectsDeferred.complete(Unit)
            },
            onError = {
                it.printStackTrace()
                projectsDeferred.complete(Unit)
            }
        )
    }

    private suspend fun loadExpenses() {
        val expensesDeferred = CompletableDeferred<Unit>()

        ExpenseService.getExpenses(
            currentUser.value.token,
            onSuccess = {
                _expenses.value = it
                Log.d("Expenses size", expenses.value.size.toString())
                // Your other logic here
                expensesDeferred.complete(Unit)
            },
            onError = {
                it.printStackTrace()
                expensesDeferred.complete(Unit)
            }
        )
        expensesDeferred.await()
    }


    private fun updateProjectTimers(expense: Expense) {
        val date = LocalDateTime.ofInstant(Instant.parse(expense.startDateTime!!), ZoneOffset.UTC)
        val startTimeInMillis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        var timeInMillis = System.currentTimeMillis() - startTimeInMillis
        if (timeInMillis < 0) {
            timeInMillis *= -1
        }

        val updatedProjectTimersStartTime = _projectTimers.value.toMutableMap()
        updatedProjectTimersStartTime[expense.projectId!!] = timeInMillis
        _projectTimers.value = updatedProjectTimersStartTime
        Log.d("Project Timer " + expense.projectId, timeInMillis.toString())
        this.setProjectStartTime(expense)
    }


    fun setProjectTime(project: Project) {
        val updatedProjectTimers = projectTimers.value.toMutableMap().apply {
            this[project.id!!] =
                System.currentTimeMillis() - projectTimersStartTime.value[project.id]!!
        }
        _projectTimers.value = updatedProjectTimers
    }

    fun setProjectStartTime(expense: Expense?) {
        val projectId = if(expense != null) {
            expense.projectId!!
        } else {
            currentProject.value!!.id!!
        }
        val updatedProjectTimers = projectTimersStartTime.value.toMutableMap().apply {
            this[projectId] =
                System.currentTimeMillis() - projectTimers.value[projectId]!!
        }
        _projectTimersStartTime.value = updatedProjectTimers
    }

    fun toggleProjectTimer(run: Boolean) {
        _projectTimersOnRun.value += (currentProject.value!!.id!! to run)
        if (currentExpense.value == null) {
            this.createNewExpense()
        } else {
            // update state
            val state = if (run) {
                "RUNNING"
            } else {
                "PAUSED"
            }
            this.updateCurrentExpense(state)
        }

    }

    private fun createNewExpense() {
        val expense = Expense(
            null,
            this.getCurrentDate(),
            null,
            "RUNNING",
            currentUser.value.id,
            currentProject.value!!.id!!,
            1L // dummy weekly timecard
        );
        this.saveExpense(expense)
    }

    private fun updateCurrentExpense(newState: String) {
        val updatedExpense = currentExpense.value!!.copy(state = newState)
        this.updateExpense(updatedExpense)
    }

    private fun saveExpense(newExpense: Expense) {
        ExpenseService.createExpense(
            expense = newExpense,
            token = currentUser.value.token,
            onSuccess = {
                _currentExpense.value = it
                val updatedExpenses = expenses.value.toMutableList().plus(it)
                _expenses.value = updatedExpenses
            },
            onError = { it.printStackTrace() }
        )
    }

    private fun updateExpense(expense: Expense) {
        ExpenseService.updateExpense(
            expense = expense,
            token = currentUser.value.token,
            onSuccess = {
                _currentExpense.value = it
                val targetIdx = expenses.value.indexOfFirst { currentExpense.value!!.id == it.id }
                val updatedExpenses = expenses.value.toMutableList()
                updatedExpenses[targetIdx] = it
                _expenses.value = updatedExpenses
            },
            onError = { it.printStackTrace() }
        )
    }

    fun changeProject(newProject: Project) {
        val timerWasRunning = projectTimersOnRun.value[currentProject.value!!.id]!!

        _projectTimersOnRun.value += (currentProject.value!!.id!! to false)
        val oldExpense = expenses.value.find { expense -> expense.projectId == currentProject.value!!.id }
        val updatedOldExpense = oldExpense!!.copy(state = "PAUSED")
        this.updateExpense(updatedOldExpense)

        _currentProject.value = newProject

        val expenseAlreadyStarted = expenses.value.find { expense ->
            (expense.projectId!! == newProject.id!!)
                    && (expense.state!! == "RUNNING" || expense.state == "PAUSED")
        }
        this.setProjectStartTime(null)
        if (expenseAlreadyStarted == null) {
            _projectTimersOnRun.value += (currentProject.value!!.id!! to timerWasRunning)
            this.createNewExpense()
        } else {
            _currentExpense.value = expenseAlreadyStarted
            this.toggleProjectTimer(timerWasRunning)
        }
    }


    fun stopAllProjectTimers() {
        _projectTimersOnRun.value = _projectTimersOnRun.value.mapValues { (_, _) -> false }
        expenses.value.forEach {expense ->
            val updatedExpense = expense.copy(state = "FINISHED", endDateTime = this.getCurrentDate())
            this.updateExpense(updatedExpense)
        }
    }

    fun getOverallTime(): Long {
        var timeOfAllProjects = 0L
        projectTimers.value.values.forEach { timeOfAllProjects += it }
        return timeOfAllProjects
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }
}