package com.example.xpense_app.view.info_view

import Expense
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpense_app.R
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import com.example.xpense_app.view.overview.GetExpenses
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

//TODO: Navigation zeigt info blau nach weiterleitung auf profiles an

/**
 * Composable function to create the info view.
 *
 * @param navController the navigation controller
 * @param user the user object
 */
@Composable
fun CreateInfoView(navController: NavController, user: MutableState<User>) {

    val expenses = remember {
        mutableStateOf(listOf<Expense>())
    }

    GetExpenses(user = user.value, expenses = expenses)

    
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            if (user.value.weeklyWorkingHours == null) {//TODO: Zu Profile ändern
                AlertDialog(
                    title = { Text(stringResource(R.string.set_weekly_workinghours)) },
                    onDismissRequest = { navController.navigate(NavigationItem.Profiles.route) },
                    confirmButton = { navController.navigate(NavigationItem.Profiles.route) })
            } else {
                UserFullNameHeader(user.value)
                Text(text = stringResource(R.string.remaining_working_hours_for_today))
                WorkingHoursCard(
                    calculateTodaysRemainingWorkingHours(
                        expenses.value,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.remaining_working_hours_for_this_week))
                WorkingHoursCard(
                    calculateThisWeeksRemainingWorkingHours(
                        expenses.value,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.remaining_working_hours_for_this_month))
                WorkingHoursCard(
                    calculateThisMonthsRemainingWorkingHours(
                        expenses.value,
                        user.value.weeklyWorkingHours ?: 0
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.worked_hours_this_month))
                WorkingHoursCard(
                    calculateThisMonthsRemainingWorkingHours(
                        expenses.value,
                        user.value.weeklyWorkingHours ?: 0,
                        true
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.role_tag))

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

/**
 * Composable function to create the user full name header.
 *
 * @param user the user object
 */
@Composable
fun UserFullNameHeader(user: User) {
    Text(
        text = "${user.prename} ${user.lastname}",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
}

/**
 * Composable function to create a working hours card.
 *
 * @param text the text to display
 */
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

/**
 * Calculates the remaining working hours for today.
 *
 * @param expenses the list of expenses
 */
@Composable
fun calculateTodaysRemainingWorkingHours(expenses: List<Expense>, weeklyWorkingHours: Int): String {
    val todayExpenses = expenses.filter { expense ->
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val startDate = startDateTime.toLocalDate()
        startDate == LocalDate.now(startDateTime.zone)
    }
    val remainingHours = weeklyWorkingHours * 60 / 5 - calculateWorkedMinutes(todayExpenses)
    return stringResource(
        R.string.remaining_hours_and_minutes,
        remainingHours / 60,
        remainingHours % 60
    )
}

/**
 * Calculates the remaining working hours for this week.
 *
 * @param expenses the list of expenses
 * @param weeklyWorkingHours the weekly working hours
 */
@Composable
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

    return stringResource(
        R.string.remaining_hours_and_minutes,
        remainingHours / 60,
        remainingHours % 60
    )
}

/**
 * Calculates the remaining working hours for this month.
 *
 * @param expenses the list of expenses
 * @param weeklyWorkingHours the weekly working hours
 * @param returnWorkedHours whether to return the worked hours
 */
@Composable
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
    val remainingMinutes = weeklyWorkingHours / 5.0 * getWorkingDaysInCurrentMonth() * 60 - calculateWorkedMinutes(thisMonthExpenses)
    return if (returnWorkedHours) {
        stringResource(
            R.string.worked_minutes_and_hours,
            calculateWorkedMinutes(thisMonthExpenses) / 60,
            calculateWorkedMinutes(
                thisMonthExpenses
            ) % 60
        )
    } else {
        stringResource(
            R.string.remaining_hours_and_minutes,
            remainingMinutes / 60,
            remainingMinutes % 60
        )
    }
}

/**
 * Calculates the worked minutes.
 *
 * @param expenses the list of expenses
 * @return the worked minutes
 */
fun calculateWorkedMinutes(expenses: List<Expense>): Long {
    return expenses.sumOf { expense ->
        if(expense.startDateTime == null || expense.endDateTime == null) {
            return@sumOf 0
        }
        val startDateTime = ZonedDateTime.parse(expense.startDateTime)
        val endDateTime = ZonedDateTime.parse(expense.endDateTime)
        val duration = Duration.between(startDateTime, endDateTime)
        duration.toMinutes()
    }
}

/**
 * Get the number of working days in the current month.
 *
 * @return the number of working days
 */
fun getWorkingDaysInCurrentMonth(): Int {
    val today = LocalDate.now()
    val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
    val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())

    var workingDaysCount = 0
    var currentDay = firstDayOfMonth

    while (!currentDay.isAfter(lastDayOfMonth)) {
        if (currentDay.dayOfWeek != DayOfWeek.SATURDAY && currentDay.dayOfWeek != DayOfWeek.SUNDAY) {
            workingDaysCount++
        }
        currentDay = currentDay.plusDays(1)
    }

    return workingDaysCount
}


