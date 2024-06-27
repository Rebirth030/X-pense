package com.example.xpense_app.view.timer.view_model

import Expense
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimerViewModel(private val currentUser: MutableState<User>) : ViewModel() {
    private lateinit var context: Context

    //var errorMessage: String = "";

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

    /**
     * Sets current context for string resources.
     *
     * @param con current context.
     */
    fun setContext(con: Context) {
        this.context = con
    }

    /**
     * Loads projects and updates the related state variables.
     *
     * This method asynchronously fetches projects from the `ProjectService` using the current user's token.
     * On successful retrieval, it updates various state variables, including `_projects`, `_currentProject`,
     * `_projectTimersOnRun`, `_projectTimersStartTime`, and `_projectTimers`. Additionally, it processes open
     * expenses to manage their states and associated timers.
     *
     * In case of an error during project retrieval, it updates the `errorMessage` with the appropriate message
     * and clears the `_projects` state.
     *
     * The method uses a `CompletableDeferred<Unit>` object to signal the completion of the project loading process.
     *
     * Note: This method catches and handles `IllegalAccessException` during the processing of the retrieved projects
     * and expenses.
     */
    private fun loadProjects() {
        val projectsDeferred = CompletableDeferred<Unit>()
        ProjectService.getProjects(
            currentUser.value.token,
            onSuccess = {
                try {
                    requireNotNull(it)
                    _projects.value = it
                    _currentProject.value = _projects.value[0]
                    it.map { project ->
                        requireNotNull(project.id)
                        _projectTimersOnRun.value += (project.id to false)
                        _projectTimersStartTime.value += (project.id to 0L)
                        _projectTimers.value += (project.id to 0L)
                    }

                    val openExpenses =
                        expenses.value.filter { expense ->
                            expense.state == "RUNNING" || expense.state == "PAUSED"
                        }
                    if (openExpenses.isNotEmpty()) {
                        openExpenses.forEach { expense ->
                            val expenseProjectId = requireNotNull(expense.projectId) {
                                this.context.getString(R.string.error_expense_has_no_project)
                            }
                            val expensePausedAtTimestamp = requireNotNull(expense.pausedAtTimestamp) {
                                this.context.getString(R.string.expense_paused_time_must_not_be_null)
                            }
                            if (expense.state == "RUNNING") {
                                _currentExpense.value = expense
                                _currentProject.value =
                                    projects.value.find { project -> project.id == expenseProjectId }
                                _projectTimersOnRun.value += (expenseProjectId to true)
                                this.updateProjectTimers(expense)
                            } else {
                                _projectTimersOnRun.value += (expenseProjectId to false)
                                val updatedTimer = _projectTimers.value.toMutableMap()
                                updatedTimer[expense.projectId] = expensePausedAtTimestamp
                                this.updateProjectTimers(expense)
                                _projectTimers.value = updatedTimer
                            }
                            projectsDeferred.complete(Unit)
                        }
                    }
                } catch (e: IllegalAccessException) {
                    _projects.value = emptyList()
                    projectsDeferred.complete(Unit)
                }
            },
            onError = {
                projectsDeferred.complete(Unit)
            }
        )
    }

    /**
     * Loads expenses and updates the related state variables.
     *
     * This suspending method asynchronously fetches expenses from the `ExpenseService` using the current user's token.
     * On successful retrieval, it updates the `_expenses` state variable. In case of an error, it prints the stack trace
     * and completes the `expensesDeferred` object.
     *
     * Note: This method is suspending and should be called from a coroutine or another suspending function.
     */
    private suspend fun loadExpenses() {
        val expensesDeferred = CompletableDeferred<Unit>()

        ExpenseService.getExpenses(
            currentUser.value.token,
            onSuccess = {
                _expenses.value = it
                expensesDeferred.complete(Unit)
            },
            onError = {
                it.printStackTrace()
                expensesDeferred.complete(Unit)
            }
        )
        expensesDeferred.await()
    }

    /**
     * Updates the project timers based on the provided expense.
     *
     * This method calculates the time elapsed since the start of the expense and updates the project timers accordingly.
     * It ensures that the start time of the expense and the project ID are not null and then calculates the elapsed time.
     * The method handles any `IllegalArgumentException` that may occur during the process.
     *
     * @param expense The expense object used to update the project timers.
     * @throws IllegalArgumentException If the start time or project ID of the expense is null.
     */
    private fun updateProjectTimers(expense: Expense) {
        try {
            val expenseStartTime = requireNotNull(expense.startDateTime) {
                this.context.getString(R.string.error_while_reading_expense_start_time)
            }
            val date = LocalDateTime.ofInstant(Instant.parse(expenseStartTime), ZoneId.systemDefault())
            val startTimeInMillis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            var timeInMillis = System.currentTimeMillis() - startTimeInMillis
            if (timeInMillis < 0) {
                timeInMillis *= -1
            }

            val expenseProjectId = requireNotNull(expense.projectId) {
                this.context.getString(R.string.error_expense_has_no_project)
            }
            val updatedProjectTimersStartTime = _projectTimers.value.toMutableMap()
            updatedProjectTimersStartTime[expenseProjectId] = timeInMillis
            _projectTimers.value = updatedProjectTimersStartTime
            this.setProjectStartTime(expense)
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }

    /**
     * Sets the project time based on the provided project.
     *
     * This method updates the project timers by calculating the elapsed time since the project's timer start time.
     * It ensures that the project ID and the project timer start time are not null before performing the calculation.
     * The method handles any `IllegalArgumentException` that may occur during the process.
     *
     * @param project The project object used to set the project time.
     * @throws IllegalArgumentException If the project ID or project timer start time is null.
     */
    fun setProjectTime(project: Project) {
        try {
            val projectId = requireNotNull(project.id) {
                this.context.getString(R.string.project_id_must_not_be_null)
            }
            val projectStartTimer = requireNotNull(projectTimersStartTime.value[projectId]) {
                this.context.getString(R.string.project_timer_must_not_be_null)
            }
            val updatedProjectTimers = projectTimers.value.toMutableMap().apply {
                this[projectId] = System.currentTimeMillis() - projectStartTimer
            }
            _projectTimers.value = updatedProjectTimers
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }

    /**
     * Sets the start time for a project based on the provided expense or the current project.
     *
     * This method updates the project timer start times by calculating the elapsed time since the current project timer
     * or the expense's project timer. It ensures that the project ID and the project timer are not null before performing
     * the calculation. The method handles any `IllegalArgumentException` that may occur during the process.
     *
     * @param expense The expense object used to set the project start time. If null, the current project is used.
     * @throws IllegalArgumentException If the project ID or project timer is null.
     */
    fun setProjectStartTime(expense: Expense?) {
        try {
            val projectId = if (expense != null) {
                requireNotNull(expense.projectId) {
                    this.context.getString(R.string.error_expense_has_no_project)
                }
                expense.projectId
            } else {
                val currentProjectVal = requireNotNull(currentProject.value) {
                    this.context.getString(R.string.current_project_must_not_be_null)
                }
                requireNotNull(currentProjectVal.id) {
                    this.context.getString(R.string.current_project_must_not_be_null)
                }
                currentProjectVal.id
            }
            val currentTimer = requireNotNull(projectTimers.value[projectId]) {
                this.context.getString(R.string.project_timer_must_not_be_null)
            }
            val updatedProjectTimers = projectTimersStartTime.value.toMutableMap().apply {
                this[projectId] = System.currentTimeMillis() - currentTimer
            }
            _projectTimersStartTime.value = updatedProjectTimers
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }

    /**
     * Toggles the timer for the current project between running and paused states.
     *
     * This method updates the `_projectTimersOnRun` state to set the timer for the current project as running or paused.
     * If there is no current expense, it creates a new expense. If there is a current expense, it updates its state
     * to either "RUNNING" or "PAUSED" based on the `run` parameter.
     *
     * @param run A boolean indicating whether the timer should be running (true) or paused (false).
     * @throws IllegalArgumentException If the current project or its ID is null.
     */
    fun toggleProjectTimer(run: Boolean) {
        try {
            val currentProjectValue = requireNotNull(currentProject.value) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }
            val currentProjectId = requireNotNull(currentProjectValue.id) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }
            _projectTimersOnRun.value += (currentProjectId to run)

            val expense = expenses.value.find { expense -> expense.projectId == currentProjectId && expense.endDateTime == null}
            if (expense == null) {
                _projectTimersOnRun.value += (currentProjectId to true)
                _projectTimersStartTime.value += (currentProjectId to System.currentTimeMillis())
                _projectTimers.value += (currentProjectId to 0L)
                val newExpense = requireNotNull(this.createNewExpense()) {
                    this.context.getString(R.string.current_project_must_not_be_null)
                }
                _currentExpense.value = newExpense
            } else {
                _currentExpense.value = expense
                val state = if (run) {
                    "RUNNING"
                } else {
                    "PAUSED"
                }
                this.updateCurrentExpense(state)
            }
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }

    /**
     * Creates a new expense for the current user and project.
     *
     * This method constructs a new `Expense` object with the current date and sets its state to "RUNNING".
     * It ensures that the current user and project, along with their IDs, are not null before creating the expense.
     * The method handles any `IllegalArgumentException` that may occur during the process.
     *
     * @throws IllegalArgumentException If the current user, their ID, the current project, or its ID is null.
     */
    private fun createNewExpense(state: String? = "RUNNING"): Expense? {
        try {
            val currentUser = requireNotNull(currentUser.value) {
                this.context.getString(R.string.error_while_laoding_user)
            }
            val currentUserId = requireNotNull(currentUser.id) {
                this.context.getString(R.string.error_while_laoding_user)
            }
            val currentProject = requireNotNull(currentProject.value) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }
            val currentProjectId = requireNotNull(currentProject.id) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }
            val expense = Expense(
                null,
                this.getCurrentDate(),
                null,
                state,
                currentUserId,
                currentProjectId,
            );
            this.saveExpense(expense)
            return expense
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
        return null
    }

    /**
     * Updates the state of the current expense.
     *
     * This method modifies the current expense's state to the provided `newState`. If the new state is "PAUSED", it also
     * updates the `pausedAtTimestamp` of the expense with the current project timer. The updated expense is then saved.
     * The method ensures that the current expense is not null before making the update.
     * The method handles any `IllegalArgumentException` that may occur during the process.
     *
     * @param newState The new state to set for the current expense. It can be "RUNNING" or "PAUSED".
     * @throws IllegalArgumentException If the current expense is null.
     */
    private fun updateCurrentExpense(newState: String) {
        try {
            val currentExpense = requireNotNull(currentExpense.value) {
                this.context.getString(R.string.project_id_of_expense_must_not_be_null)
            }
            val updatedExpense = if (newState == "PAUSED") {
                val currentTimestamp = projectTimers.value[currentExpense.projectId]
                currentExpense.copy(state = newState, pausedAtTimestamp = currentTimestamp)
            } else {
                currentExpense.copy(state = newState)
            }
            this.updateExpense(updatedExpense)
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }

    /**
     * Saves a new expense using the `ExpenseService`.
     *
     * This method calls the `ExpenseService.createExpense` function to save the provided `newExpense`.
     * It uses the current user's token for authentication and handles the success and error callbacks appropriately.
     *
     * On success:
     * - Updates the `_currentExpense` state with the newly created expense.
     * - Adds the new expense to the `_expenses` state list.
     * - Resets the `errorMessage` to an empty string.
     *
     * On error:
     * - Sets the `errorMessage` to "Error while save Expense".
     *
     * @param newExpense The new expense object to be saved.
     */
    private fun saveExpense(newExpense: Expense) {
        ExpenseService.createExpense(
            expense = newExpense,
            token = currentUser.value.token,
            onSuccess = {
                _currentExpense.value = it
                val updatedExpenses = expenses.value.toMutableList().plus(it)
                _expenses.value = updatedExpenses
            },
            onError = {
                this.showErrorToast(context.getString(R.string.error_while_creating_expense))
            }
        )
    }

    /**
     * Updates an existing expense using the `ExpenseService`.
     *
     * This method calls the `ExpenseService.updateExpense` function to update the provided `expense`.
     * It uses the current user's token for authentication and handles the success and error callbacks appropriately.
     * The method ensures that the current expense is not null before proceeding with the update.
     *
     * On success:
     * - Updates the `_currentExpense` state with the updated expense.
     * - Finds the index of the current expense in the `_expenses` list and updates it with the new expense.
     * - Resets the `errorMessage` to an empty string.
     *
     * On error:
     * - Throws an `IllegalArgumentException` with the message "Could not update expense."
     *
     * @param expense The expense object to be updated.
     * @throws IllegalArgumentException If the current expense is null.
     */
    private fun updateExpense(expense: Expense) {
        val expensesDeferred = CompletableDeferred<Unit>()
        try {
            ExpenseService.updateExpense(
                expense = expense,
                token = currentUser.value.token,
                onSuccess = {
                    _currentExpense.value = it
                    val targetIdx = expenses.value.indexOfFirst { expense.id == it.id }
                    val updatedExpenses = expenses.value.toMutableList()
                    updatedExpenses[targetIdx] = it
                    _expenses.value = updatedExpenses
                    expensesDeferred.complete(Unit)
                },
                onError = {
                    this.showErrorToast(it.message ?: this.context.getString(R.string.error_unkown))
                    expensesDeferred.complete(Unit)
                }
            )
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
            expensesDeferred.complete(Unit)
        }
    }

    /**
     * Changes the current project to the provided new project.
     *
     * This method updates the current project to the provided `newProject`. It also updates related expenses and project
     * timers based on the state of the current and new projects. The method ensures that the current project is not null
     * before proceeding with the change.
     *
     * @param newProject The new project object to change to.
     * @throws IllegalArgumentException If the current project is null, or if there is no matching expense for the
     * current project, or if the ID of the new project is null.
     */
    fun changeProject(newProject: Project) {
        try {
            val currentProject = requireNotNull(currentProject.value) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }
            requireNotNull(currentProject.id) {
                this.context.getString(R.string.current_project_must_not_be_null)
            }

            _expenses.value = expenses.value.filter { expense -> expense.state != "FINISHED" }
            val timerWasRunning = projectTimersOnRun.value[currentProject.id] ?: false
            _projectTimersOnRun.value += (currentProject.id to false)
            requireNotNull(newProject.id) {
                this.context.getString(R.string.error_while_createing_project)
            }
            _currentProject.value = newProject
            val oldExpense = expenses.value.find { expense -> expense.projectId == currentProject.id }
            if(oldExpense != null) {
                val updatedOldExpense =
                    oldExpense.copy(state = "PAUSED", pausedAtTimestamp = _projectTimers.value[oldExpense.projectId])
                this.updateExpense(updatedOldExpense)
            } else {
                _currentExpense.value = requireNotNull(this.createNewExpense("PAUSED")) {
                    this.context.getString(R.string.error_while_creating_expense)
                }
            }


            val expenseAlreadyStarted = expenses.value.find { expense ->
                (expense.projectId == newProject.id)
                        && (expense.state == "RUNNING" || expense.state == "PAUSED")
            }
            this.setProjectStartTime(null)

            if (expenseAlreadyStarted == null) {
                _projectTimersOnRun.value += (newProject.id to timerWasRunning)
                this.createNewExpense()
            } else {
                _currentExpense.value = expenseAlreadyStarted
                this.toggleProjectTimer(timerWasRunning)
            }
        } catch (e: IllegalArgumentException) {
            this.showErrorToast(e.message ?: this.context.getString(R.string.error_unkown))
        }
    }


    /**
     * Reorders the list of projects with the current project as the first element.
     *
     * This method rearranges the list of projects so that the current project is placed at the beginning.
     * It ensures that the current project is not null before proceeding with the reordering.
     *
     * @return The reordered list of projects with the current project at the beginning.
     */
    fun reorderProjects(): List<Project> {
        try {
            val currentProject = requireNotNull(currentProject.value)
            val reorderedProjects = projects.value.toMutableList()
            reorderedProjects.clear()
            reorderedProjects.add(currentProject)
            for (project in projects.value) {
                if (project.id != currentProject.id) {
                    reorderedProjects.add(project)
                }
            }
            _projects.value = reorderedProjects
            return reorderedProjects
        } catch (e: IllegalArgumentException) {
            return _projects.value
        }
    }

    /**
     * Stops all project timers and finishes all running expenses.
     *
     * This method sets all project timers to false in the `_projectTimersOnRun` state, indicating that all timers are
     * stopped. It also updates the state of all running expenses to "FINISHED" and sets their end date time to the current
     * date time. The method does not return any value.
     */
    fun stopAllProjectTimers() {
        _projectTimersOnRun.value = _projectTimersOnRun.value.mapValues { (_, _) -> false }
        _expenses.value = expenses.value.map { expense ->
            if (expense.state == "RUNNING" || expense.state == "PAUSED") {
                val updatedExpense = expense.copy(state = "FINISHED", endDateTime = getCurrentDate())
                updateExpense(updatedExpense)
                updatedExpense
            } else {
                expense
            }
        }

        this.loadProjects()
    }

    /**
     * Calculates the overall time across all projects.
     *
     * This method sums up the time durations of all projects stored in the `_projectTimers` state and returns the total
     * time in milliseconds. It does not consider the current project's timer state.
     *
     * @return The overall time across all projects in milliseconds.
     */
    fun getOverallTime(): Long {
        var timeOfAllProjects = 0L
        projectTimers.value.values.forEach { timeOfAllProjects += it }
        return timeOfAllProjects
    }

    /**
     * Retrieves the current date and time as a formatted string.
     *
     * This method generates a formatted string representing the current date and time in the format "yyyy-MM-dd'T'HH:mm:ss'Z'".
     * It uses the `LocalDateTime.now()` function to get the current date and time and formats it using a `DateTimeFormatter`.
     * The formatted string is then returned.
     *
     * @return The current date and time as a formatted string.
     */
    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val currentDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault())
        return currentDateTime.format(formatter)
    }

    /**
     * Shows error toast.
     *
     * @param message of the error toast.
     */
    private fun showErrorToast(message: String) {
        runBlocking {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}