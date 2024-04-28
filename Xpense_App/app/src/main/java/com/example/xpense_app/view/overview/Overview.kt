package com.example.xpense_app.view.overview

import Expense
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.roundToInt

const val EXPENSE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
val INPUT_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME
val HOUR_HEIGT = 65.dp
val DAY_WIDTH = 256.dp

@Composable
fun CreateOverview(user: User, navController: NavController, padding: PaddingValues) {
    val now = remember { mutableStateOf(LocalDateTime.now()) }
    val expenses = remember { mutableStateOf(listOf<Expense>()) }
    val displayedWeekNumber = remember {
        mutableIntStateOf(
            now.value.get(
                WeekFields.of(Locale.getDefault()).weekBasedYear()
            )
        )
    }
    val currentStartOfWeek = remember { mutableStateOf(now.value.with(DayOfWeek.MONDAY)) }
    val currentEndOfWeek = remember { mutableStateOf(now.value.with(DayOfWeek.SUNDAY)) }

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            val context = LocalContext.current
            GetExpenses(user, expenses, context)
            Schedule(
                expenses = expenses.value,
                startDate = currentStartOfWeek.value,
                endDate = currentEndOfWeek.value
            )
        }
    }
}

@Composable
fun GetExpenses(user: User, expenses: MutableState<List<Expense>>, context: Context) {
    LaunchedEffect(key1 = "getExpenses") {
        withContext(Dispatchers.IO) {
            ExpenseService.getExpenses(
                token = user.token,
                onSuccess = { expenseList ->
                    expenses.value = expenseList
                }, onError = { e ->
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })

        }
    }
}

@Composable
fun Schedule(
    expenses: List<Expense>,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    Column(modifier = modifier) {
        ScheduleHeader(
            startDate = startDate,
            endDate = endDate,
            modifier = Modifier.horizontalScroll(horizontalScrollState)
        )
        DailySchedule(
            expenses = expenses,
            startDate = startDate,
            endDate = endDate,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
        )
    }
}

@Composable
fun BasicDayHeader(
    day: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    Text(
        text = day.format(DateTimeFormatter.ofPattern("E, MMM d")),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Composable
fun ScheduleHeader(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    modifier: Modifier = Modifier,
    days: Int = 7,
    dayHeader: @Composable (day: LocalDateTime) -> Unit = { BasicDayHeader(day = it) },
) {
    Row(modifier = modifier) {
        repeat(days) { i ->
            Box(modifier = Modifier.width(DAY_WIDTH)) {
                dayHeader(startDate.plusDays(i.toLong()))
            }
        }
    }
}

@Composable
fun DailySchedule(
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    expenseCard: @Composable (expense: Expense) -> Unit = { ExpenseCard(expense = it) },
    startDate: LocalDateTime,
    endDate: LocalDateTime
) {
    Layout(
        content = {
            expenses.sortedBy(Expense::startDateTime).forEach { expense ->
                Box(modifier = Modifier.expenseData(expense)) {
                    expenseCard(expense)
                }
            }
        },
        modifier = modifier,
    ) { measureables, constraints ->
        val height = HOUR_HEIGT.roundToPx() * 24
        val width = DAY_WIDTH.roundToPx() * 7
        val placeables = measureables.map { measurable ->
            val expense = measurable.parentData as Expense
            val startDateTime = parseDateTime(expense.startDateTime)
            val endDateTime = parseDateTime(expense.endDateTime)
            val expenseDurationMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime)
            val expenseHeight = ((expenseDurationMinutes / 60f) * HOUR_HEIGT.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minHeight = expenseHeight,
                    maxHeight = expenseHeight,
                    minWidth = DAY_WIDTH.roundToPx(),
                    maxWidth = DAY_WIDTH.roundToPx()
                )
            )
            Pair(placeable, expense)
        }
        layout(width, height) {
            placeables.forEach { (placeable, expense) ->
                val startDateTime = parseDateTime(expense.startDateTime)
                val expenseOffsetMinutes =
                    ChronoUnit.MINUTES.between(LocalTime.MIN, startDateTime.toLocalTime())
                val expenseY = ((expenseOffsetMinutes / 60f) * HOUR_HEIGT.toPx()).roundToInt()
                val expenseOffSetDays = ChronoUnit.DAYS.between(startDate, startDateTime)
                val expenseX = expenseOffSetDays * DAY_WIDTH.roundToPx()
                placeable.place(expenseX.toInt(), expenseY)
            }
        }
    }
}

fun parseDateTime(dateTime: String?): LocalDateTime {
    return if (dateTime != null) {
        LocalDateTime.parse(dateTime, INPUT_DATE_TIME_FORMAT)
    } else {
        LocalDateTime.now()
    }
}

@Composable
fun ExpenseCard(expense: Expense, modifier: Modifier = Modifier) {
    Card {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(end = 2.dp, bottom = 2.dp)
                .background(Color.Cyan, shape = RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {
            // TODO : Make prettier maybe get Project instead of only id
            Text(
                text = parseDateTime(expense.startDateTime).format(
                    DateTimeFormatter.ofPattern(
                        EXPENSE_TIME_FORMAT
                    )
                )
            )
            Text(
                text = parseDateTime(expense.endDateTime).format(
                    DateTimeFormatter.ofPattern(
                        EXPENSE_TIME_FORMAT
                    )
                )
            )
            Text(text = expense.description)
            Text(text = expense.projectId.toString())
        }
    }
}

private class ExpenseDataModifier(private val expense: Expense) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = expense
}

private fun Modifier.expenseData(expense: Expense) = this.then(ExpenseDataModifier(expense))