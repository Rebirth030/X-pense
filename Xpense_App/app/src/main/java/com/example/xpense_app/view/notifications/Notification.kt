
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.xpense_app.model.User
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

private const val CHANNEL_ID = "MyAppChannel"
private const val notificationId = 1
fun checkForNotifications(
    currentUser: MutableState<User>,
    context: Context,
    timerViewModel: TimerViewModel
) {
    val currentDateTimeMillis = System.currentTimeMillis()


    if (currentUser.value.forcedEndAfterOn == true && currentUser.value.weeklyWorkingHours?.let {
            currentWeekWorkedHours(timerViewModel) > it
        } == true) {
        showNotification(
            "Weekly working hours exceeded",
            "You have exceeded the weekly working hours.",
            context
        )
    }

    if (currentUser.value.forcedBreakAfterOn == true && currentUser.value.forcedBreakAfter?.let {
            currentBreakTime(timerViewModel) > it * 60 * 60 * 1000
        } == true) {
        showNotification(
            "Forced break time exceeded",
            "You have exceeded the forced break time.",
            context
        )
    }

    if (currentUser.value.forcedEndAfterOn == true && currentUser.value.forcedEndAfter?.let {
            val forcedEndAfterMillis = it * 60 * 60 * 1000
            val currentEndTimeMillis = currentEndTime(timerViewModel)?.let {
                parseDateTime(it).toEpochSecond(
                    ZoneOffset.UTC
                ) * 1000
            }
            currentEndTimeMillis != null && currentDateTimeMillis - currentEndTimeMillis > forcedEndAfterMillis
        } == true) {
        showNotification(
            "Forced end time exceeded",
            "You have exceeded the forced end time.",
            context
        )
    }

}


fun currentWeekWorkedHours(timerViewModel: TimerViewModel): Int {
    val currentWeekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY)
    val currentWeekEnd = LocalDate.now().with(java.time.DayOfWeek.SUNDAY)

    var totalWorkedHours = 0
    for (expense in timerViewModel.expenses.value) {
        val startTime = expense.startDateTime?.let { parseDateTime(it) }
        val endTime = expense.endDateTime?.let { parseDateTime(it) }
        if (startTime != null && endTime != null) {
            val expenseDate = startTime.toLocalDate()
            if (expenseDate in currentWeekStart..currentWeekEnd) {
                val duration = calculateDuration(startTime, endTime)
                totalWorkedHours += duration
            }
        }
    }
    return totalWorkedHours
}

fun currentBreakTime(timerViewModel: TimerViewModel): Long {
    var totalBreakTime = 0L
    for (expense in timerViewModel.expenses.value) {
        if (expense.pausedAtTimestamp != null) {
            totalBreakTime += expense.pausedAtTimestamp
        }
    }
    return totalBreakTime
}

fun currentEndTime(timerViewModel: TimerViewModel): String? {
    var latestEndTime: String? = null
    for (expense in timerViewModel.expenses.value) {
        if (expense.endDateTime != null) {
            if (latestEndTime == null || latestEndTime < expense.endDateTime) {
                latestEndTime = expense.endDateTime
            }
        }
    }
    return latestEndTime
}

fun parseDateTime(dateTimeString: String): java.time.LocalDateTime {
    return java.time.LocalDateTime.parse(dateTimeString)
}

fun calculateDuration(startTime: java.time.LocalDateTime, endTime: java.time.LocalDateTime): Int {
    val durationInSeconds = java.time.Duration.between(startTime, endTime).seconds
    return (durationInSeconds / 3600).toInt() // Convert seconds to hours
}

private fun showNotification(title: String, content: String, context: Context) {
    createNotificationChannel(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, builder.build())
    }
}

private fun createNotificationChannel(context: Context) {
    val name = "MyApp Notifications"
    val descriptionText = "General notifications for MyApp"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
    }

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

object NotificationService {
    private var notificationJob: Job? = null

    fun startNotificationChecking(context: Context, currentUser: MutableState<User>, timerViewModel: TimerViewModel) {
        notificationJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                checkForNotifications(currentUser, context, timerViewModel)
                delay(1 * 60 * 1000) // Check notifications every 1 minutes
            }
        }
    }
}
