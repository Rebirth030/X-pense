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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import com.example.xpense_app.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor

/**
 * Displays the main profile page for the current user.
 *
 * This composable function is responsible for rendering the profile page UI. It takes the current user's state
 * and displays various components that represent the user's profile information.
 *
 * @param currentUser A [MutableState] containing the [User] object for the currently logged-in user.
 */
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
        mutableStateOf(currentUser.value.role == UserRole.CUSTOM.name)
    }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        DropDown(
            currentUser,
            onSelectedProfile = {userRole ->
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
        if (!userHasCostumedProfile || (role != UserRole.CUSTOM.name)) {
            ImmutableAndInitialCustomProfileInput(
                role,
                weeklyWorkingHours?: 40,
                forcedBreakAfter?: 4.0,
                forcedBreakAfterOn?: false,
                forcedEndAfter?: 8.0,
                forcedEndAfterOn?: false,
                onForcedBreakChanged = { forcedBreakAfter = it },
                onForcedBreakToggle = { forcedBreakAfterOn = it },
                onForcedEndChanged = { forcedEndAfter = it },
                onForcedEndToggle = { forcedEndAfterOn = it },
                onWeeklyWorkingHoursChanged = { weeklyWorkingHours = it }
            )
        } else {
            CustomProfileInput(
                weeklyWorkingHours?: 40,
                forcedBreakAfter?: 4.0,
                forcedBreakAfterOn?: false,
                forcedEndAfter?: 8.0,
                forcedEndAfterOn?: false,
                onForcedBreakChanged = { forcedBreakAfter = it },
                onForcedBreakToggle = { forcedBreakAfterOn = it },
                onForcedEndChanged = { forcedEndAfter = it },
                onForcedEndToggle = { forcedEndAfterOn = it },
                onWeeklyWorkingHoursChanged = { weeklyWorkingHours = it }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        NotificationInput(
            notificationsOn?: false,
            onSwitchToggle = { notificationsOn = it }
        )
        Spacer(modifier = Modifier.weight(1f))
        SaveButton(
            onSaveUser = {
                try {
                    val weeklyWorkingHoursValue = requireNotNull(weeklyWorkingHours) { "Weekly working hours must not be null" }
                    val forcedBreakAfterValue = requireNotNull(forcedBreakAfter) { "Forced break after must not be null" }
                    val forcedEndAfterValue = requireNotNull(forcedEndAfter) { "Forced end after must not be null" }
                    val forcedBreakAfterOnValue = requireNotNull(forcedBreakAfterOn) { "Forced break after must not be null" }
                    val forcedEndAfterOnValue = requireNotNull(forcedEndAfterOn) { "Forced end after must not be null" }
                    val notificationsOnValue = requireNotNull(notificationsOn) { "Notification must not be null" }

                    require(forcedBreakAfterValue <= forcedEndAfterValue) { "Forced break can't be greater than forced end" }
                    require(forcedEndAfterValue <= weeklyWorkingHoursValue) { "Forced end can't be greater than weekly working hours" }
                    require(forcedBreakAfterValue <= weeklyWorkingHoursValue) { "Forced break can't be greater than weekly working hours" }

                    saveUser(
                        context,
                        currentUser,
                        role,
                        weeklyWorkingHoursValue,
                        forcedBreakAfterValue,
                        forcedBreakAfterOnValue,
                        forcedEndAfterValue,
                        forcedEndAfterOnValue,
                        notificationsOnValue
                    )
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}

/**
 * Displays input elements for customizing the profile settings.
 *
 * This composable function is responsible for rendering input elements that allow the user to customize
 * various profile settings such as role, weekly working hours, forced break settings, etc.
 *
 * @param role The role of the user.
 * @param weeklyWorkingHours The number of weekly working hours for the user.
 * @param forcedBreakAfter The duration after which a forced break occurs.
 * @param forcedBreakAfterOn A boolean indicating whether forced breaks are enabled.
 * @param forcedEndAfter The duration after which a forced end occurs.
 * @param forcedEndAfterOn A boolean indicating whether forced ends are enabled.
 * @param onForcedBreakChanged Callback function triggered when the forced break duration is changed.
 * @param onForcedBreakToggle Callback function triggered when the forced break is toggled.
 * @param onForcedEndChanged Callback function triggered when the forced end duration is changed.
 * @param onForcedEndToggle Callback function triggered when the forced end is toggled.
 * @param onWeeklyWorkingHoursChanged Callback function triggered when the weekly working hours are changed.
 */
@Composable
fun ImmutableAndInitialCustomProfileInput(
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
        "Forced break after",
        role != UserRole.CUSTOM.name,
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
        "Forced end after",
        role != UserRole.CUSTOM.name,
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
        "Weekly\nworking hours",
        role != UserRole.CUSTOM.name,
        weeklyWorkingHours,
        onInputChange = {
            onWeeklyWorkingHoursChanged(it)
        }
    )
}

/**
 * Displays input elements for customizing profile settings.
 *
 * This composable function is responsible for rendering input elements that allow the user to customize
 * various profile settings such as weekly working hours, forced break settings, etc.
 *
 * @param weeklyWorkingHours The number of weekly working hours for the user.
 * @param forcedBreakAfter The duration after which a forced break occurs.
 * @param forcedBreakAfterOn A boolean indicating whether forced breaks are enabled.
 * @param forcedEndAfter The duration after which a forced end occurs.
 * @param forcedEndAfterOn A boolean indicating whether forced ends are enabled.
 * @param onForcedBreakChanged Callback function triggered when the forced break duration is changed.
 * @param onForcedBreakToggle Callback function triggered when the forced break toggle is changed.
 * @param onForcedEndChanged Callback function triggered when the forced end duration is changed.
 * @param onForcedEndToggle Callback function triggered when the forced end toggle is changed.
 * @param onWeeklyWorkingHoursChanged Callback function triggered when the weekly working hours are changed.
 */
@Composable
fun CustomProfileInput(
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
        "Forced break after",
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
        "Forced end after",
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
            text = "Weekly\nworking hours:",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        MutableHourInput(weeklyWorkingHours, onInputSelected = { onWeeklyWorkingHoursChanged(it) })
    }
}

/**
 * Updates the input fields of the profile with the provided user role and current user state.
 *
 * This function is responsible for updating the input fields of the profile based on the provided user role
 * and the current state of the user.
 *
 * @param role The role of the user.
 * @param currentUser A [MutableState] containing the [User] object for the currently logged-in user.
 * @param onForcedBreakChanged Callback function triggered when the forced break duration is changed.
 * @param onForcedBreakToggle Callback function triggered when the forced break toggle is changed.
 * @param onForcedEndChanged Callback function triggered when the forced end duration is changed.
 * @param onForcedEndToggle Callback function triggered when the forced end toggle is changed.
 * @param onWeeklyWorkingHoursChanged Callback function triggered when the weekly working hours are changed.
 * @param onNotificationsOn Callback function triggered when notifications are toggled.
 */
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

        UserRole.CUSTOM -> {
            if (currentUser.value.role != UserRole.CUSTOM.name) {
                onWeeklyWorkingHoursChanged(0)
                onForcedBreakChanged(0.0)
                onForcedBreakToggle(false)
                onForcedEndChanged(0.0)
                onForcedEndToggle(false)
                onNotificationsOn(false)
            } else {
                onWeeklyWorkingHoursChanged(currentUser.value.weeklyWorkingHours?: 0)
                onForcedBreakChanged(currentUser.value.forcedBreakAfter?: 0.0)
                onForcedBreakToggle(currentUser.value.forcedBreakAfterOn?: false)
                onForcedEndChanged(currentUser.value.forcedEndAfter?: 0.0)
                onForcedEndToggle(currentUser.value.forcedEndAfterOn?: false)
                onNotificationsOn(currentUser.value.notification?: false)
            }
        }
    }
}


/**
 * Saves the user's profile data.
 *
 * This function is responsible for saving the user's profile data, including role, weekly working hours,
 * forced break settings, forced end settings, and notification preferences.
 *
 * @param context The context of the application.
 * @param currentUser A [MutableState] containing the [User] object for the currently logged-in user.
 * @param role The role of the user.
 * @param weeklyWorkingHours The number of weekly working hours for the user.
 * @param forcedBreakAfter The duration after which a forced break occurs.
 * @param forcedBreakAfterOn A boolean indicating whether forced breaks are enabled.
 * @param forcedEndAfter The duration after which a forced end occurs.
 * @param forcedEndAfterOn A boolean indicating whether forced ends are enabled.
 * @param notificationsOn A boolean indicating whether notifications are enabled.
 */
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
                    "Profile successfully updated to: ${it.role}",
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        onError = {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "An Error has occurred while updating profile!: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}