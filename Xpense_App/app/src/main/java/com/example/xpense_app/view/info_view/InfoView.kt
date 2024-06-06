package com.example.xpense_app.view.info_view

import Expense
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime


@Composable
fun CreateInfoView(navController: NavController, user: MutableState<User>) {

    val expenses = remember {
        mutableListOf<Expense>()
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        ExpenseService.getExpenses(token = user.value.token,
            onSuccess = { expenses.addAll(it) },
            onError = {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "Error loading expenses", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    //TODO: Navigation zeigt info blau nach weiterleitung auf profiles an
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            if (user.value.weeklyWorkingHours == 0) {//TODO: Zu Profile ändern
                AlertDialog(
                    title = { Text("Please set your weekly working hours in the profile") },
                    onDismissRequest = { navController.navigate(NavigationItem.Profiles.route) },
                    confirmButton = { navController.navigate(NavigationItem.Profiles.route) })
            } else {
                UserFullNameHeader(user.value)
                Text(text = "Remaining working hours for today:")
                WorkingHoursCard(
                    calculateTodaysRemainingWorkingHours(
                        expenses,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Remaining working hours for this week:")
                WorkingHoursCard(
                    calculateThisWeeksRemainingWorkingHours(
                        expenses,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Remaining working hours for this month:")
                WorkingHoursCard(
                    calculateThisMonthsRemainingWorkingHours(
                        expenses,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Worked hours this Month:")
                WorkingHoursCard(
                    calculateThisMonthsRemainingWorkingHours(
                        expenses,
                        user.value.weeklyWorkingHours ?: 0,
                        true
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Role:")
                Card(
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp, horizontal = 16.dp
                        )
                        .fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = user.value.role,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UserFullNameHeader(user: User) {
    Text(
        text = "${user.prename} ${user.lastname}",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun WorkingHoursCard(text: String) {
    Card(
        modifier = Modifier
            .padding(
                vertical = 4.dp, horizontal = 16.dp
            )
            .fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
    }
}

fun calculateTodaysRemainingWorkingHours(expenses: List<Expense>, weeklyWorkingHours: Int): String {
    val todayExpenses = expenses.filter { expense ->
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val startDate = startDateTime.toLocalDate()
        startDate == LocalDate.now(startDateTime.zone)
    }
    val remainingHours = weeklyWorkingHours * 60 / 5 - calculateWorkedMinutes(todayExpenses)
    return "${remainingHours / 60} hours and ${remainingHours % 60} minutes"
}

fun calculateThisWeeksRemainingWorkingHours(
    expenses: List<Expense>,
    weeklyWorkingHours: Int
): String {
    val now = LocalDate.now()
    val startOfWeek = now.with(DayOfWeek.MONDAY)
    val endOfWeek = now.with(DayOfWeek.SUNDAY)
    val thisWeekExpenses = expenses.filter { expense ->
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val startDate = startDateTime.toLocalDate()
        startDate.isAfter(startOfWeek.minusDays(1)) && startDate.isBefore(endOfWeek.plusDays(1))
    }
    val remainingHours = weeklyWorkingHours * 60 - calculateWorkedMinutes(thisWeekExpenses)
    return "${remainingHours / 60} hours and ${remainingHours % 60} minutes"
}

fun calculateThisMonthsRemainingWorkingHours(
    expenses: List<Expense>,
    weeklyWorkingHours: Int,
    returnWorkedHours: Boolean = false
): String {
    val now = LocalDate.now()
    val thisMonthExpenses = expenses.filter { expense ->
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val startDate = startDateTime.toLocalDate()
        startDate.year == now.year && startDate.month == now.month
    }
    val remainingHours = weeklyWorkingHours * 60 - calculateWorkedMinutes(thisMonthExpenses)
    if (returnWorkedHours) {
        return "${calculateWorkedMinutes(thisMonthExpenses) / 60} hours and ${
            calculateWorkedMinutes(
                thisMonthExpenses
            ) % 60
        } minutes"
    } else {
        return "${remainingHours / 60} hours and ${remainingHours % 60} minutes"
    }
}

fun calculateWorkedMinutes(expenses: List<Expense>): Long {
    return expenses.sumOf { expense ->
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val endDateTime = ZonedDateTime.parse(expense.endDateTime)
        val duration = Duration.between(startDateTime, endDateTime)
        duration.toMinutes()
    }
}


