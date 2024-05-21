package com.example.xpense_app.view.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import com.example.xpense_app.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor

@Composable
@ExperimentalMaterial3Api
fun Profile(currentUser: MutableState<User>) {
    val context = LocalContext.current
    var role by remember {
        mutableStateOf(currentUser.value.role)
    }
    var forcedEndAfter by remember {
        mutableStateOf(currentUser.value.forcedEndAfter)
    }
    var forcedEndAfterOn by remember {
        mutableStateOf(currentUser.value.forcedEndAfterOn)
    }
    var forcedBreakAfter by remember {
        mutableStateOf(currentUser.value.forcedBreakAfter)
    }
    var forcedBreakAfterOn by remember {
        mutableStateOf(currentUser.value.forcedBreakAfterOn)
    }
    var notificationsOn by remember {
        mutableStateOf(currentUser.value.notification)
    }
    var weeklyWorkingHours by remember {
        mutableStateOf(currentUser.value.weeklyWorkingHours)
    }
    val userHasCostumedProfile by remember {
        mutableStateOf(currentUser.value.role == UserRole.COSTUMED.name)
    }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp)) // header
        DropDown(
            currentUser,
            onSelectedProfile = { userRole ->
                role = userRole.name
                updateProfileInputFields(userRole, currentUser,
                                         onForcedBreakChanged = { forcedBreakAfter = it },
                                         onForcedBreakToggle = { forcedBreakAfterOn = it },
                                         onForcedEndChanged = { forcedEndAfter = it },
                                         onForcedEndToggle = { forcedEndAfterOn = it },
                                         onWeeklyWorkingHoursChanged = { weeklyWorkingHours = it },
                                         onNotificationsOn = { notificationsOn = it }
                )
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        if (!userHasCostumedProfile || (role != UserRole.COSTUMED.name)) {
            ImmutableAndInitialCostumedProfileInput(
                role,
                weeklyWorkingHours!!,
                forcedBreakAfter!!,
                forcedBreakAfterOn!!,
                forcedEndAfter!!,
                forcedEndAfterOn!!,
                onForcedBreakChanged = { forcedBreakAfter = it },
                onForcedBreakToggle = { forcedBreakAfterOn = it },
                onForcedEndChanged = { forcedEndAfter = it },
                onForcedEndToggle = { forcedEndAfterOn = it },
                onWeeklyWorkingHoursChanged = { weeklyWorkingHours = it }
            )
        } else {
            CostumedProfileInput(
                weeklyWorkingHours!!,
                forcedBreakAfter!!,
                forcedBreakAfterOn!!,
                forcedEndAfter!!,
                forcedEndAfterOn!!,
                onForcedBreakChanged = { forcedBreakAfter = it },
                onForcedBreakToggle = { forcedBreakAfterOn = it },
                onForcedEndChanged = { forcedEndAfter = it },
                onForcedEndToggle = { forcedEndAfterOn = it },
                onWeeklyWorkingHoursChanged = { weeklyWorkingHours = it }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        NotificationInput(
            notificationsOn!!,
            onSwitchToggle = { notificationsOn = it }
        )
        Spacer(modifier = Modifier.weight(1f))
        SaveButton(
            onSaveUser = {
                if (inputsAreValid(
                        context,
                        weeklyWorkingHours!!,
                        forcedBreakAfter!!,
                        forcedEndAfter!!
                    )
                ) {
                    saveUser(
                        context, currentUser, role,
                        weeklyWorkingHours!!,
                        forcedBreakAfter!!,
                        forcedBreakAfterOn!!,
                        forcedEndAfter!!,
                        forcedEndAfterOn!!,
                        notificationsOn!!
                    )
                }
            })
    }
}

@Composable
fun ImmutableAndInitialCostumedProfileInput(
    role: String,
    weeklyWorkingHours: Int,
    forcedBreakAfter: Double,
    forcedBreakAfterOn: Boolean,
    forcedEndAfter: Double,
    forcedEndAfterOn: Boolean,
    onForcedBreakChanged: (Double) -> Unit,
    onForcedBreakToggle: (Boolean) -> Unit,
    onForcedEndChanged: (Double) -> Unit,
    onForcedEndToggle: (Boolean) -> Unit,
    onWeeklyWorkingHoursChanged: (Int) -> Unit
) {
    HourMinuteInput(
        stringResource(R.string.forced_break_after),
        role != UserRole.COSTUMED.name,
        floor(forcedBreakAfter).toInt(),
        ((forcedBreakAfter - floor(forcedBreakAfter)) * 60).toInt(),
        forcedBreakAfterOn,
        onInputChange = {
            onForcedBreakChanged(it)
        },
        onSwitchToggle = {
            onForcedBreakToggle(it)
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
    HourMinuteInput(
        stringResource(R.string.forced_end_after),
        role != UserRole.COSTUMED.name,
        floor(forcedEndAfter).toInt(),
        ((forcedEndAfter - floor(forcedEndAfter)) * 60).toInt(),
        forcedEndAfterOn,
        onInputChange = {
            onForcedEndChanged(it)
        },
        onSwitchToggle = {
            onForcedEndToggle(it)
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
    HourInput(
        stringResource(R.string.weekly_working_hours),
        role != UserRole.COSTUMED.name,
        weeklyWorkingHours,
        onInputChange = {
            onWeeklyWorkingHoursChanged(it)
        }
    )
}

@Composable
fun CostumedProfileInput(
    weeklyWorkingHours: Int,
    forcedBreakAfter: Double,
    forcedBreakAfterOn: Boolean,
    forcedEndAfter: Double,
    forcedEndAfterOn: Boolean,
    onForcedBreakChanged: (Double) -> Unit,
    onForcedBreakToggle: (Boolean) -> Unit,
    onForcedEndChanged: (Double) -> Unit,
    onForcedEndToggle: (Boolean) -> Unit,
    onWeeklyWorkingHoursChanged: (Int) -> Unit
) {
    MutableHourMinuteInput(
        stringResource(R.string.forced_break_after),
        floor(forcedBreakAfter).toInt(),
        ((forcedBreakAfter - floor(forcedBreakAfter)) * 60).toInt(),
        forcedBreakAfterOn,
        onInputChange = {
            onForcedBreakChanged(it)
        },
        onSwitchToggle = {
            onForcedBreakToggle(it)
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
    MutableHourMinuteInput(
        stringResource(R.string.forced_end_after),
        floor(forcedEndAfter).toInt(),
        ((forcedEndAfter - floor(forcedEndAfter)) * 60).toInt(),
        forcedEndAfterOn,
        onInputChange = {
            onForcedEndChanged(it)
        },
        onSwitchToggle = {
            onForcedEndToggle(it)
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
    Row(modifier = Modifier.padding(10.dp))
    {
        Text(
            text = stringResource(R.string.weekly_working_hours),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        MutableHourInput(weeklyWorkingHours, onInputSelected = { onWeeklyWorkingHoursChanged(it) })
    }
}

fun updateProfileInputFields(
    role: UserRole,
    currentUser: MutableState<User>,
    onForcedBreakChanged: (Double) -> Unit,
    onForcedBreakToggle: (Boolean) -> Unit,
    onForcedEndChanged: (Double) -> Unit,
    onForcedEndToggle: (Boolean) -> Unit,
    onWeeklyWorkingHoursChanged: (Int) -> Unit,
    onNotificationsOn: (Boolean) -> Unit
) {
    when (role) {
        UserRole.EMPLOYEE -> {
            onWeeklyWorkingHoursChanged(40)
            onForcedBreakChanged(4.0)
            onForcedBreakToggle(true)
            onForcedEndChanged(8.0)
            onForcedEndToggle(true)
            onNotificationsOn(false)
        }

        UserRole.FREELANCER -> {
            onWeeklyWorkingHoursChanged(41)
            onForcedBreakChanged(5.0)
            onForcedBreakToggle(false)
            onForcedEndChanged(10.0)
            onForcedEndToggle(false)
            onNotificationsOn(false)
        }

        UserRole.WORK_STUDENT -> {
            onWeeklyWorkingHoursChanged(20)
            onForcedBreakChanged(3.0)
            onForcedBreakToggle(false)
            onForcedEndChanged(6.0)
            onForcedEndToggle(false)
            onNotificationsOn(false)
        }

        UserRole.COSTUMED -> {
            if (currentUser.value.role != UserRole.COSTUMED.name) {
                onWeeklyWorkingHoursChanged(0)
                onForcedBreakChanged(0.0)
                onForcedBreakToggle(false)
                onForcedEndChanged(0.0)
                onForcedEndToggle(false)
                onNotificationsOn(false)
            } else {
                onWeeklyWorkingHoursChanged(currentUser.value.weeklyWorkingHours!!)
                onForcedBreakChanged(currentUser.value.forcedBreakAfter!!)
                onForcedBreakToggle(currentUser.value.forcedBreakAfterOn!!)
                onForcedEndChanged(currentUser.value.forcedEndAfter!!)
                onForcedEndToggle(currentUser.value.forcedEndAfterOn!!)
                onNotificationsOn(currentUser.value.notification!!)
            }
        }
    }
}


fun saveUser(
    context: Context,
    currentUser: MutableState<User>,
    role: String,
    weeklyWorkingHours: Int,
    forcedBreakAfter: Double,
    forcedBreakAfterOn: Boolean,
    forcedEndAfter: Double,
    forcedEndAfterOn: Boolean,
    notificationsOn: Boolean
) {
    currentUser.value.role = role
    currentUser.value.weeklyWorkingHours = weeklyWorkingHours
    currentUser.value.forcedBreakAfter = forcedBreakAfter
    currentUser.value.forcedBreakAfterOn = forcedBreakAfterOn
    currentUser.value.forcedEndAfter = forcedEndAfter
    currentUser.value.forcedEndAfterOn = forcedEndAfterOn
    currentUser.value.notification = notificationsOn
    UserService.updateUser(
        currentUser.value,
        currentUser.value.token,
        onSuccess = {
            currentUser.value = it
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.profile_successfully_updated, it.role),
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        onError = {
            it.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_message_an_error_has_occurred_while_updating_profile, it.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}

fun inputsAreValid(
    context: Context,
    weeklyWorkingHours: Int,
    forcedBreakAfter: Double,
    forcedEndAfter: Double
): Boolean {
    return try {
        require(forcedBreakAfter <= forcedEndAfter) { context.getString(R.string.error_message_forced_break_cant_be_greater_then_forced_end) }
        require(forcedEndAfter <= weeklyWorkingHours) { context.getString(R.string.error_message_forced_end_cant_be_greater_then_weekly_working_hours) }
        true
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        false
    }
}