package com.example.xpense_app.view.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp)) // header
        DropDown(
            currentUser,
            onSelectedProfile = {
                role = it.name
                when (it) {
                    UserRole.EMPLOYEE -> {
                        weeklyWorkingHours = 40
                        forcedBreakAfter = 4.0
                        forcedBreakAfterOn = true
                        forcedEndAfter = 8.0
                        forcedEndAfterOn = true
                        notificationsOn = false
                    }

                    UserRole.FREELANCER -> {
                        weeklyWorkingHours = 41
                        forcedBreakAfter = 5.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 10.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }

                    UserRole.WORK_STUDENT -> {
                        Log.d("CHANGE ROLE", "Helo")
                        weeklyWorkingHours = 20
                        forcedBreakAfter = 3.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 6.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }

                    UserRole.COSTUMED -> {
                        weeklyWorkingHours = 0
                        forcedBreakAfter = 0.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 0.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        HourMinuteInput(
            "Forced break after",
            role != UserRole.COSTUMED.name,
            floor(forcedBreakAfter!!).toInt(),
            ((forcedBreakAfter!! - floor(forcedBreakAfter!!)) * 60).toInt(),
            forcedBreakAfterOn!!,
            onInputChange = {
                forcedBreakAfter = it
            },
            onSwitchToggle = {
                forcedBreakAfterOn = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HourMinuteInput(
            "Forced end after",
            role != UserRole.COSTUMED.name,
            floor(forcedEndAfter!!).toInt(),
            ((forcedEndAfter!! - floor(forcedEndAfter!!)) * 60).toInt(),
            forcedEndAfterOn!!,
            onInputChange = {
                forcedEndAfter = it
            },
            onSwitchToggle = {
                forcedEndAfterOn = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HourInput(
            "Weekly\nworking hours",
            role != UserRole.COSTUMED.name,
            weeklyWorkingHours!!,
            onInputChange = {
                weeklyWorkingHours = it
            }
        )
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
                    )) {
                    currentUser.value.role = role
                    currentUser.value.weeklyWorkingHours = weeklyWorkingHours
                    currentUser.value.forcedBreakAfter = forcedBreakAfter
                    currentUser.value.forcedBreakAfterOn = forcedBreakAfterOn
                    currentUser.value.forcedEndAfter = forcedEndAfter
                    currentUser.value.forcedEndAfterOn = forcedEndAfterOn
                    currentUser.value.notification = false // default of every profile
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
                            it.printStackTrace()
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
            })
    }
}

fun inputsAreValid(
    context: Context,
    weeklyWorkingHours: Int,
    forcedBreakAfter: Double,
    forcedEndAfter: Double
): Boolean {
    Log.d("Forced break", forcedBreakAfter.toString())
    Log.d("Forced end", forcedEndAfter.toString())
    Log.d("Weekly working hour", weeklyWorkingHours.toString())
    if (forcedBreakAfter > forcedEndAfter) {
        Toast.makeText(context, "Forced break cant be greater then forced end.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (forcedEndAfter > weeklyWorkingHours) {
        Toast.makeText(context, "Forced end cant be greater then weekly working hours.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (forcedBreakAfter > weeklyWorkingHours) {
        Toast.makeText(context, "Forced break cant be greater then weekly working hours.", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}