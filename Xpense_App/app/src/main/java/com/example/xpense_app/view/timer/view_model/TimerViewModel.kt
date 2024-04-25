package com.example.xpense_app.view.timer.view_model

import Expense
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.log

class TimerViewModel : ViewModel() {
    private val projectService = ProjectService()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _newExpense = MutableStateFlow<Expense?>(null)
    val newExpense: StateFlow<Expense?> = _newExpense.asStateFlow()

    private val _projectTimersOnRun = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val projectTimersOnRun: StateFlow<Map<Long, Boolean>> = _projectTimersOnRun.asStateFlow()

    private val _projectTimersStartTime = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val projectTimersStartTime: StateFlow<Map<Long, Long>> = _projectTimersStartTime.asStateFlow()

    private val _projectTimers = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val projectTimers: StateFlow<Map<Long, Long>> = _projectTimers.asStateFlow()

    init {
        this.loadProjects()
    }

    fun loadProjects() {
        projectService.getProjects(
            onSuccess = {
                _projects.value = it
                if (_projects.value.isNotEmpty()) {
                    _currentProject.value = _projects.value[0]
                }
                it.map { project ->
                    _projectTimersOnRun.value += (project.id!! to false)
                    _projectTimersStartTime.value += (project.id to 0L)
                    _projectTimers.value += (project.id to 0L)
                }
                val d = 0;
            },
            onError = {
                val e = it
            }
        )
    }

    fun isCurrentProjectOnRun(): Boolean {
        return projectTimersOnRun.value[currentProject.value!!.id!!]!!
    }


    fun setProjectTime() {
        val updatedProjectTimers = projectTimers.value.toMutableMap().apply {
            this[currentProject.value!!.id!!] =
                System.currentTimeMillis() - projectTimersStartTime.value[currentProject.value!!.id!!]!!
        }
        _projectTimers.value = updatedProjectTimers
        projectTimers.value.map {
            val tag = "TIMER ( Project id: " + it.key + " )"
            Log.d(tag, this.formatTime(it.value))

        }
    }

    fun setProjectStartTime() {
        val updatedProjectTimers = projectTimersStartTime.value.toMutableMap().apply {
            this[currentProject.value!!.id!!] =
                System.currentTimeMillis() - projectTimers.value[currentProject.value!!.id!!]!!
        }
        _projectTimersStartTime.value = updatedProjectTimers
        projectTimersStartTime.value.map {
            val tag = "START TIME ( Project id: " + it.key + " )"
            Log.d(tag, this.formatTime(it.value))
        }
    }

    fun toggleProjectTimer(run: Boolean) {
        _projectTimersOnRun.value += (currentProject.value!!.id!! to run)
    }

    fun stopAllProjectTimers() {
        _projectTimersOnRun.value = _projectTimersOnRun.value.mapValues { (_, _) -> false }
    }

    fun getCurrentProjectTime(): Long {
        return projectTimers.value[currentProject.value!!.id!!]!!
    }

    fun createNewExpense() {
        val c = currentProject.value
        val expense = Expense(
            null,
            LocalDateTime.now().toString(),
            null, // end date will be set bei stop button
            "RUNNING",
            1L, // dummy user
            currentProject.value!!.id!!,
            1L // dummy weekly timecard
        );
        _newExpense.value = expense
        val updatedExpenses = _expenses.value.toMutableList().plus(expense)
        _expenses.value = updatedExpenses
    }

    fun toggleState() {
        val state = if (this.isCurrentProjectOnRun()) {
            "RUNNING"
        } else {
            "PAUSED"
        }
        _newExpense.value = _newExpense.value!!.copy(state = state)
    }

    /**
     * TODO: Save Expense in database
     */
    fun updateExpense() {
        _newExpense.value =
            _newExpense.value!!.copy(endDateTime = LocalDateTime.now().toString())


        // save Expense in database
    }

    /**
     * TODO: Handle Project change
     */
    fun changeProject(newProject: Project) {
        // change status of expense (endDate & state)
        // save expense in database
        _currentProject.value = newProject
        // create new expense & timer stays running if timer was running
        // reset timers
    }

    //Logging method
    fun formatTime(timeMi: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeMi)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMi) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}