package com.example.xpense_app.view.overview

import Expense
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.model.User
import com.example.xpense_app.view.parseDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.exp
import kotlin.math.roundToInt

const val EXPENSE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
const val HOUR_FORMAT = "HH:mm"
val HOUR_HEIGHT = 65.dp
val DAY_WIDTH = 256.dp

//TODO: Only fetch expenses for current user and current week

/**
 * CreateOverview creates the overview screen
 * @param user the user to display the overview for and to fetch the expenses
 * @param navController the NavController to navigate to other screens
 * @param padding the padding to apply to the screen
 */
@Composable
fun CreateOverview(user: User, navController: NavController, padding: PaddingValues) {
    val now = remember { mutableStateOf(LocalDateTime.now()) }
    val expenses = remember { mutableStateOf(listOf<Expense>()) }
    val currentStartOfWeek = remember { mutableStateOf(now.value.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0)) }

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            GetExpenses(user, expenses)
            WeekSelection(currentStartOfWeek)
            Schedule(expenses.value, currentStartOfWeek.value)
        }
    }
}

/**
 * WeekSelection allows the user to navigate through the weeks
 * @param currentStartOfWeek the start DateTime of the current week
 */
@Composable
fun WeekSelection(currentStartOfWeek: MutableState<LocalDateTime>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            currentStartOfWeek.value = currentStartOfWeek.value.minusWeeks(1)
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text("Week ${currentStartOfWeek.value.format(DateTimeFormatter.ofPattern("w"))}")
        IconButton(onClick = { currentStartOfWeek.value = currentStartOfWeek.value.plusWeeks(1) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
        }
    }
}

/**
 * Composable that displays the expenses of a week in a schedule together with a sidebar and header
 * @param expenses the list of expenses to display
 * @param startDate the first day of the week to be shown
 * @param endDate the last day of the week to be shown
 * @param startTime the time that the schedule should start to display
 * @param endTime the time that the schedule should end to display
 */
@Composable
fun Schedule(
    expenses: List<Expense>,
    startDate: LocalDateTime,
    endDate: LocalDateTime = startDate.plusDays(7),
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.MIN,
    endTime: LocalTime = LocalTime.MAX
) {
    val sidebarWidth by remember { mutableIntStateOf(0) }
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    Column(modifier = modifier) {
        ScheduleHeader(
            startDate = startDate,
            endDate = endDate,
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
        )
        Row(modifier = Modifier.weight(1f)) {
            ScheduleSidebar(
                startTime = startTime,
                endTime = endTime,
                modifier = Modifier.verticalScroll(verticalScrollState)
            )
            WeeklySchedule(
                expenses = expenses,
                startDate = startDate,
                endDate = endDate,
                startTime = startTime,
                endTime = endTime,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            )
        }
    }
}

/**
 * Sidebar that displays the hours of the day
 * @param startTime the time that the schedule should start to display
 * @param endTime the time that the schedule should end to display
 */
@Composable
fun ScheduleSidebar(
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.MIN,
    endTime: LocalTime = LocalTime.MAX
) {
    Column(modifier = modifier) {
        val timesToRepeat = ChronoUnit.HOURS.between(startTime, endTime).toInt() + 1
        repeat(timesToRepeat) { i ->
            Box(modifier = Modifier.height(HOUR_HEIGHT)) {
                Text(
                    text = startTime.plusHours(i.toLong())
                        .format(DateTimeFormatter.ofPattern(HOUR_FORMAT)),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp)
                )
            }
        }
    }
}

/**
 * Header that displays the days of the week
 * @param startDate the first day of the week to be shown
 * @param endDate the last day of the week to be shown
 */
@Composable
fun ScheduleHeader(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    val days = ChronoUnit.DAYS.between(startDate, endDate).toInt()
    Row(modifier = modifier) {
        repeat(days) { i ->
            Box(modifier = Modifier.width(DAY_WIDTH)) {
                Text(
                    text = startDate.plusDays(i.toLong())
                        .format(DateTimeFormatter.ofPattern("E, MMM d")),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }
    }
}

/**
 * WeeklySchedule displays the expenses in a schedule
 * @param expenses the list of expenses to display
 * @param startDate the first day of the week to be shown
 * @param endDate the last day of the week to be shown
 * @param startTime the time that the schedule should start to display
 * @param endTime the time that the schedule should end to display
 * @param expenseCard the composable that displays the expense
 */
@Composable
fun WeeklySchedule(
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    expenseCard: @Composable (expense: Expense) -> Unit = { ExpenseCard(expense = it) },
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    startTime: LocalTime = LocalTime.MIN,
    endTime: LocalTime = LocalTime.MAX
) {
    val hours = ChronoUnit.HOURS.between(startTime, endTime).toInt() + 1
    val days = ChronoUnit.DAYS.between(startDate, endDate).toInt()
    Layout(
        content = {
            expenses.sortedBy(Expense::startDateTime).forEach { expense ->
                Box(modifier = Modifier.expenseData(expense)) {
                    expenseCard(expense)
                }
            }
        },
        modifier = modifier.drawBehind {
            repeat(hours - 1) {
                drawLine(
                    Color.Gray.copy(alpha = 0.5f),
                    start = Offset(0f, (it + 1) * HOUR_HEIGHT.toPx()),
                    end = Offset(size.width, (it + 1) * HOUR_HEIGHT.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            repeat(days - 1) {
                drawLine(
                    Color.Gray.copy(alpha = 0.5f),
                    start = Offset((it + 1) * DAY_WIDTH.toPx(), 0f),
                    end = Offset((it + 1) * DAY_WIDTH.toPx(), size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        },
    ) { measureables, constraints ->
        placeExpenses(measureables, constraints, startDate, endDate, startTime, endTime)
    }
}

/**
 * Places the expenses in the schedule
 * @param measureables the list of Measurables to place
 * @param constraints the constraints of the layout
 * @param startDate the first day of the week to be shown
 * @param endDate the last day of the week to be shown
 * @param startTime the time that the schedule should start to display
 * @param endTime the time that the schedule should end to display
 */
private fun MeasureScope.placeExpenses(
    measureables: List<Measurable>,
    constraints: Constraints,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    startTime: LocalTime = LocalTime.MIN,
    endTime: LocalTime = LocalTime.MAX
): MeasureResult {
    val totalHeight = HOUR_HEIGHT.roundToPx() * (ChronoUnit.HOURS.between(startTime, endTime).toInt() +1)
    val totalWidth = DAY_WIDTH.roundToPx() * ChronoUnit.DAYS.between(startDate, endDate).toInt()
    val placeables = measureables.map { measurable ->
        val expense = measurable.parentData as Expense
        val startDateTime = parseDateTime(expense.startDateTime)
        val endDateTime = parseDateTime(expense.endDateTime)
        val expenseDurationMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime)
        val expenseHeight = ((expenseDurationMinutes / 60f) * HOUR_HEIGHT.toPx()).roundToInt()
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
    return layout(totalWidth, totalHeight) {
        placeables.forEach { (placeable, expense) ->
            val startDateTime = parseDateTime(expense.startDateTime)
            val expenseOffsetMinutes =
                ChronoUnit.MINUTES.between(startTime, startDateTime.toLocalTime())
            val expenseY = ((expenseOffsetMinutes / 60f) * HOUR_HEIGHT.toPx()).roundToInt()
            val expenseOffSetDays = ChronoUnit.DAYS.between(startDate, startDateTime)
            val expenseX = expenseOffSetDays * DAY_WIDTH.roundToPx()
            placeable.place(expenseX.toInt(), expenseY)
        }
    }
}

/**
 * GetExpenses fetches the expenses of the user
 * @param user the user to fetch the expenses for
 * @param expenses the list of expenses to update
 */
@Composable
fun GetExpenses(user: User, expenses: MutableState<List<Expense>>) {
    val context = LocalContext.current
    LaunchedEffect(key1 = "getExpenses") {
        withContext(Dispatchers.IO) {
            ExpenseService.getExpenses(token = user.token, onSuccess = { expenseList ->
                expenses.value = expenseList
            }, onError = { e ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

/**
 * ExpenseCard displays an expense in a card
 * @param expense the expense to display
 */
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
                text = "Startzeit: ${parseDateTime(expense.startDateTime).format(
                    DateTimeFormatter.ofPattern(
                        EXPENSE_TIME_FORMAT
                    )
                )}"
            )
            Text(
                text = "Endzeit: ${parseDateTime(expense.endDateTime).format(
                    DateTimeFormatter.ofPattern(
                        EXPENSE_TIME_FORMAT
                    )
                )}"
            )
            Text(text = "Beschreibung: ${expense.description.orEmpty()}")
            Text(text = "Projektnummer: ${expense.projectId.toString()}")
        }
    }
}

/**
 * creates a custom Modifier that adds the expense data to the modifier
 * @param expense the expense to add to the modifier
 */
private class ExpenseDataModifier(private val expense: Expense) : ParentDataModifier {
    /**
     * overwrites the expense data to the modifier
     */
    override fun Density.modifyParentData(parentData: Any?) = expense
}


/**
 * extension function on Modifier that adds the ExpenseDataModifier to the chain of modifiers
 * @param expense the expense to add to the modifier
 */
private fun Modifier.expenseData(expense: Expense) = this.then(ExpenseDataModifier(expense))